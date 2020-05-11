package com.fasten.wp4.optimizator.tactical.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasten.wp4.database.model.Delivery;
import com.fasten.wp4.database.model.Demand;
import com.fasten.wp4.database.model.Granularity;
import com.fasten.wp4.database.model.Part;
import com.fasten.wp4.database.model.ProcessingPart;
import com.fasten.wp4.database.model.RemoteStation;
import com.fasten.wp4.database.model.SRAM;
import com.fasten.wp4.database.model.TacticalOptimization;
import com.fasten.wp4.database.model.TacticalOptimizationResult;
import com.fasten.wp4.database.repository.DeliveryRepository;
import com.fasten.wp4.database.repository.DemandRepository;
import com.fasten.wp4.database.repository.PartRepository;
import com.fasten.wp4.database.repository.ProcessingPartRepository;
import com.fasten.wp4.database.repository.RemoteStationRepository;
import com.fasten.wp4.database.repository.SRAMRepository;
import com.fasten.wp4.database.repository.TacticalOptimizationRepository;
import com.fasten.wp4.database.repository.TacticalOptimizationResultRepository;
import com.fasten.wp4.email.client.api.EmailControllerApi;
import com.fasten.wp4.email.client.model.Email;
import com.fasten.wp4.optimizator.tactical.business.ExcelReader;
import com.fasten.wp4.optimizator.tactical.business.ExcelWriter;
import com.fasten.wp4.optimizator.tactical.business.ScriptWriter;
import com.fasten.wp4.optimizator.tactical.exception.NotFoundException;
import com.fasten.wp4.optimizator.tactical.simulator.ResultsAnalysis;
import com.fasten.wp4.predictive.client.api.ForecastApi;
import com.fasten.wp4.predictive.client.model.DemandData;
import com.fasten.wp4.predictive.client.model.ForecastStudyList;
import com.fasten.wp4.predictive.client.model.ForecastingStudy;

import io.swagger.annotations.ApiOperation;

@RestController
public class TacticalOptimizationController {
	
	@Autowired
	private TacticalOptimizationRepository tacticalOptimizationRepository;

	@Autowired
	private SRAMRepository sramRepository;
	
	@Autowired
	private DemandRepository demandRepository;
	
	@Autowired
	private RemoteStationRepository remoteStationRepository;

	@Autowired
	private PartRepository partRepository;

	@Autowired
	private DeliveryRepository deliveryRepository;
	
	@Autowired
	private ProcessingPartRepository processingPartRepository;
	
	@Autowired
	private TacticalOptimizationResultRepository tacticalOptimizationResultRepository;

	@Autowired
	private EmailControllerApi emailControllerApi;
	
	@Autowired
	private ForecastApi forecastApi;
	
	@Value("${path.oplrun:C:\\Program Files\\IBM\\ILOG\\CPLEX_Studio128\\opl\\bin\\x64_win64}")
	private String pathOplrun;
	
	@Value("${run.simulation:true}")
	private boolean runSimulation;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private Date toDate(String toFormat) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(toFormat);
		} catch (ParseException e) {
			return null;
		}
	}
	
	@GetMapping("/execute")
	@ApiOperation(nickname="executeTacticalOptimization", value = "Execute a tactical optimization")
	@Transactional
	public ResponseEntity<TacticalOptimizationResult> executeTacticalOptimization(@RequestParam String requestId,@RequestParam Long tacticalOptimizationId, String usermail) throws Exception {
		
		LOGGER.info("Starting optimization");
		LOGGER.info("Getting data");
		//retrieve database
		TacticalOptimization tacticalOptimization = tacticalOptimizationRepository.findById(tacticalOptimizationId).get();
		List<Demand> demands = demandRepository.retrieveDemandByTacticalOptimization(tacticalOptimizationId);
		List<RemoteStation> remoteStations = remoteStationRepository.retrieveRemoteStationByTacticalOptimization(tacticalOptimizationId);
		List<Delivery> deliveries = deliveryRepository.retrieveDeliveryMatrixByTacticalOptimization(tacticalOptimizationId);
		List<Part> parts = partRepository.retrievePartByTacticalOptimization(tacticalOptimizationId);
		List<SRAM> srams = sramRepository.findAll();
		List<ProcessingPart> processingParts= processingPartRepository.retrieveAllOrderBySramCode();
		
		if(tacticalOptimization.getUsePrediction()!=null && tacticalOptimization.getUsePrediction()) {
			LOGGER.info("Starting prediction");
			List<ForecastingStudy> forecastingStudies = new ArrayList<ForecastingStudy>();
			Map<RemoteStation, Map<Part, List<Demand>>>  group = demands.stream().collect(Collectors.groupingBy(Demand::getRemoteStation, Collectors.groupingBy(Demand::getPart)));

			group.forEach((remote, map) -> {
				map.forEach((part,demandsList)->{

					ForecastingStudy forecastingStudy = new ForecastingStudy();
					List<DemandData> demandData = demandsList.stream().map(d->new DemandData().demandDate(new SimpleDateFormat("dd/MM/yyyy").format(d.getOrderDate())).demandValue(d.getQuantity())).collect(Collectors.toList());
					forecastingStudy.setDemands(demandData);
					forecastingStudy.setPart(part.getName());
					forecastingStudy.setRemoteStation(remote.getName());
					forecastingStudy.setFrequency((Granularity.Annual.equals(tacticalOptimization.getGranularity()))? ForecastingStudy.FrequencyEnum.A :
						(Granularity.Monthly.equals(tacticalOptimization.getGranularity()))? ForecastingStudy.FrequencyEnum.M :
							(Granularity.Weekly.equals(tacticalOptimization.getGranularity()))? ForecastingStudy.FrequencyEnum.W :
								(Granularity.Daily.equals(tacticalOptimization.getGranularity()))? ForecastingStudy.FrequencyEnum.D :
									null);
					forecastingStudy.setHorizon(tacticalOptimization.getHorizon());
					forecastingStudies.add(forecastingStudy);
				});
			});
			
			ForecastStudyList forecastStudyList = new ForecastStudyList();
			forecastStudyList.setStartDate(new SimpleDateFormat("dd/MM/yyyy").format(tacticalOptimization.getInitialDate()));
			forecastStudyList.setEndDate(new SimpleDateFormat("dd/MM/yyyy").format(tacticalOptimization.getEndDate()));
			ForecastStudyList forecastedList = forecastApi.postMulti(forecastStudyList);
			forecastedList.getStudyList().forEach(forecastedStudy -> {
				RemoteStation remoteStation = remoteStationRepository.findByName(forecastedStudy.getRemoteStation()).get();
				Part part = partRepository.findByName(forecastedStudy.getPart()).get();
				List<Demand> demandsForecasted = forecastedStudy.getDemands().stream().map(dd->new Demand(null,remoteStation,toDate(dd.getDemandDate()), toDate(dd.getDemandDate()), part, dd.getDemandValue(),null)).collect(Collectors.toList());
				demands.addAll(demandsForecasted);
			});
			LOGGER.info("Finished prediction");
		}

		//creates dir
		Path file = Paths.get("studies",requestId);
		Files.createDirectories(file);
		
		//TODO using first SRAM as reference, use distribuition probabilities
		Double minLeadTime = processingPartRepository.retrieveMinProcessingPartByTacticalOptimization(tacticalOptimizationId, 1l);
		
		LOGGER.info("Excell dump");
		//write xls conversions
		ExcelWriter.writeDemandExcell(requestId, tacticalOptimization, demands,remoteStations, deliveries, parts);
		ExcelWriter.writeTKExcell(requestId, tacticalOptimization, remoteStations, parts, srams, processingParts, minLeadTime);
		ExcelWriter.writeCostsExcell(requestId, deliveries);
		
		LOGGER.info("Organizing files ");
		//copy files
	    Files.copy(Paths.get("src","main","resources","p_median.dat"), Paths.get("studies",requestId,"p_median.dat"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
	    Files.copy(Paths.get("src","main","resources","p_median.mod"), Paths.get("studies",requestId,"p_median.mod"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
	    Files.copy(Paths.get("src","main","resources","TK_v9.dat"), Paths.get("studies",requestId,"TK_v9.dat"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
	    Files.copy(Paths.get("src","main","resources","TK_v9.mod"), Paths.get("studies",requestId,"TK_v9.mod"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
	    Files.copy(Paths.get("src","main","resources","TK_Case_Structure.xlsx"), Paths.get("studies",requestId,"TK_Case_Structure.xlsx"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
	  
		//write script
		ScriptWriter.createScript(requestId, pathOplrun);

		LOGGER.info("Running Optimization");
		//runs
		Process processOptimization = Runtime.getRuntime().exec( 
				new String[]{"cmd", "/C", "script.bat"},
				null, 
				Paths.get("studies",requestId).toAbsolutePath().toFile());

		BufferedReader reader = new BufferedReader(new InputStreamReader(processOptimization.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
			if(line!=null) {
				LOGGER.info(line);
			}
			line = reader.readLine();
			sb.append(line);
		}
		processOptimization.waitFor();
		LOGGER.info("Optimization finished\n");
		if(sb.toString().contains("<<< no solution")) {
			LOGGER.info("Deleting files created for running");
			//delete folder
//			FileUtils.forceDelete(Paths.get("studies",requestId).toAbsolutePath().toFile());
			Email email = new Email();
			email.setContent("Execution of Tactical Optimization study: "+ tacticalOptimization.getName() +" failed");
			email.setHtml(true);
			email.setSubject("Study finished with error");
			email.setTo(usermail);
			try {
				emailControllerApi.sendWithFreemarkerUsingPOST(email);
			} catch (com.fasten.wp4.email.client.invoker.ApiException e1) {
				LOGGER.error("Could not notify failure of tactical optimization study execution by email", e1);
			}
			throw new NotFoundException("Could not find a solution");
		}

		LOGGER.info("Reading results from Excell");
		//save the optimization result into database
		TacticalOptimizationResult tacticalOptimizationResult = ExcelReader.toPojo(Paths.get("studies",requestId,"TK_Data_v9.xlsx"));
		tacticalOptimizationResult.setStudy(tacticalOptimization);
		
		LOGGER.info("Saving optimization results do database");

		TacticalOptimizationResult savedObject = tacticalOptimizationResult;
		if(!(tacticalOptimization.getId().compareTo(11l)==0)) {
			tacticalOptimizationResult.getInternalSuppliers().forEach(item-> {
				item.setRemoteStationOrigin(remoteStationRepository.findByExcellName(item.getRemoteStationOrigin().getName()).orElse(null));
				item.setRemoteStationDestination(remoteStationRepository.findByExcellName(item.getRemoteStationDestination().getName()).orElse(null));
				item.setPart(partRepository.findByExcellName(item.getPart().getName()).orElse(null));
				item.setTacticalOptimizationResult(tacticalOptimizationResult);
			});
			
			tacticalOptimizationResult.getPrinters().forEach(item-> {
				item.setRemoteStation(remoteStationRepository.findByExcellName(item.getRemoteStation().getName()).orElse(null));
				item.setTacticalOptimizationResult(tacticalOptimizationResult);
			});
			
			tacticalOptimizationResult.getProductions().forEach(item-> {
				item.setRemoteStation(remoteStationRepository.findByExcellName(item.getRemoteStation().getName()).orElse(null));
				item.setPart(partRepository.findByExcellName(item.getPart().getName()).orElse(null));
				item.setTacticalOptimizationResult(tacticalOptimizationResult);
			});
			
			tacticalOptimizationResult.getRoutes().forEach(item-> {
				item.setRemoteStationOrigin(remoteStationRepository.findByExcellName(item.getRemoteStationOrigin().getName()).orElse(null));
				item.setRemoteStationDestination(remoteStationRepository.findByExcellName(item.getRemoteStationDestination().getName()).orElse(null));
				item.setPart(partRepository.findByExcellName(item.getPart().getName()).orElse(null));
				item.setTacticalOptimizationResult(tacticalOptimizationResult);
			});
			tacticalOptimizationResultRepository.deleteByStudy(tacticalOptimizationResult.getStudy());
			savedObject = tacticalOptimizationResultRepository.save(tacticalOptimizationResult);
		}
		
		if(runSimulation) {
			//copy folder
		    FileUtils.copyDirectory(Paths.get("src","main","resources","SimulationModel").toAbsolutePath().toFile(), Paths.get("studies",requestId,"SimulationModel").toAbsolutePath().toFile());
	
		    //then copy optimization result into simulation file
		    Files.copy(Paths.get("studies",requestId,"TK_Case_Structure.xlsx"), Paths.get("studies",requestId,"SimulationModel","TK_Case_Structure.xlsx"), StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
		    
			//Run simulation model experiment in background
		    Process processSimulation = Runtime.getRuntime().exec( 
					new String[]{"cmd", "/C", "RunExperiments.exe"},
					null, 
					Paths.get("studies",requestId,"SimulationModel").toAbsolutePath().toFile());
			BufferedReader readerSimulation = new BufferedReader(new InputStreamReader(processSimulation.getInputStream()));
			String lineSimulation = readerSimulation.readLine();
			while (lineSimulation != null) {
				if(lineSimulation!=null) {
					LOGGER.info(lineSimulation);
				}
				lineSimulation = readerSimulation.readLine();
			}
	//		processSimulation.waitFor(); //wait for run to end
			
			processSimulation.waitFor(30, TimeUnit.SECONDS);//did not work when popup lock screen
			LOGGER.info("Simulation finished\n");
	
			
			Workbook workbook = WorkbookFactory.create(new FileInputStream (Paths.get("studies",requestId,"SimulationModel","TK_Case_ResultsAnalysis_Experiment1_Scenario1_Rep1.xlsx").toAbsolutePath().toFile()));
			ResultsAnalysis resultsanalysis = new ResultsAnalysis();
			resultsanalysis.analyseExelFile(workbook);
		}
		
		LOGGER.info("Deleting files created for running");
		//delete folder
//		FileUtils.forceDelete(Paths.get("studies",requestId).toAbsolutePath().toFile());
		
		LOGGER.info("Finished execution");
		
		
		LOGGER.info("Sending email");
		Email email = new Email();
		email.setContent("Execution of Tactical Optimization study: "+ tacticalOptimization.getName() +" finished successifully");
		email.setHtml(true);
		email.setSubject("Study finished");
		email.setTo(usermail);
		try {
			emailControllerApi.sendWithFreemarkerUsingPOST(email);
		} catch (com.fasten.wp4.email.client.invoker.ApiException e) {
			LOGGER.error("Could not notify conclusion of tactical optimization study execution by email", e);
		}
		
		return ResponseEntity.ok(savedObject);
		
	}
	
}
