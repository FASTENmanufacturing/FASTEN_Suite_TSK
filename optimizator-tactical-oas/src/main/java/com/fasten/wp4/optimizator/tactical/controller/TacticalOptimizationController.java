package com.fasten.wp4.optimizator.tactical.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasten.wp4.database.model.TacticalOptimization;
import com.fasten.wp4.database.model.TacticalOptimizationResult;
import com.fasten.wp4.database.model.TacticalOptimizationType;
import com.fasten.wp4.database.repository.DeliveryRepository;
import com.fasten.wp4.database.repository.DemandRepository;
import com.fasten.wp4.database.repository.DistributionCenterRepository;
import com.fasten.wp4.database.repository.PartRepository;
import com.fasten.wp4.database.repository.ProcessingPartRepository;
import com.fasten.wp4.database.repository.RemoteStationRepository;
import com.fasten.wp4.database.repository.SRAMRepository;
import com.fasten.wp4.database.repository.TacticalOptimizationRepository;
import com.fasten.wp4.database.repository.TacticalOptimizationResultRepository;
import com.fasten.wp4.email.client.api.EmailControllerApi;
import com.fasten.wp4.email.client.model.Email;
import com.fasten.wp4.optimizator.tactical.cplex.PMedian;
import com.fasten.wp4.optimizator.tactical.model.TacticalOptimizationResponse;
import com.fasten.wp4.predictive.client.api.ForecastApi;

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
	private DistributionCenterRepository distributionCenterRepository;

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

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@PostMapping("/execute")
	@ApiOperation(nickname="execute", value = "execute tactical optimization for best cost-benefit")
	@Transactional
	public ResponseEntity<TacticalOptimizationResponse> execute(
			@RequestBody TacticalOptimization tacticalOptimization, 
			@RequestParam String requestId,
			@RequestParam(required = false) String usermail){

		Instant start_time = Instant.now();
		LOGGER.info("Starting optimization: " +tacticalOptimization.getName() + " from "+ tacticalOptimization.getInitialDate()+ " to " +tacticalOptimization.getEndDate());
		LOGGER.info("Getting data");
		int[] distributionCenters = distributionCenterRepository.retrieveDistributionCenterIdsByTacticalOptimizationOrderById(tacticalOptimization.getId());
		String[] distributionCentersNames = distributionCenterRepository.retrieveDistributionCenterNamesByTacticalOptimizationOrderById(tacticalOptimization.getId());
		int[] remoteStations = distributionCenters;
		String[] remoteStationsNames = distributionCentersNames;
		int[] parts = partRepository.retrievePartIdsByTacticalOptimizationOrderById(tacticalOptimization.getId());
		String[] partsNames =partRepository.retrievePartNamesByTacticalOptimizationOrderById(tacticalOptimization.getId());
		int[][] matrix = deliveryRepository.retrieveTimeMatrix(distributionCenters, remoteStations);
		double[] processingTime =processingPartRepository.retrieveProcessingPartsByTacticalOptimizationOrderById(tacticalOptimization.getId());
		int[][] demandByPart=demandRepository.retrieveDemandPartsByTacticalOptimizationOrderByDistributionCenterId(tacticalOptimization.getId());
		int[][] numberOfOrderByPart = demandRepository.retrieveDemandOrdersPartsByTacticalOptimizationOrderByDistributionCenterId(tacticalOptimization.getId());
		int horizon = demandRepository.retrieveHorizonByTacticalOptimization(tacticalOptimization.getId());
		int sramCapacity = tacticalOptimization.getSramCapacity().intValueExact(); //seconds
		
		Instant stop_time = Instant.now();
		LOGGER.info("Retrieve info from database time:"+ChronoUnit.MILLIS.between(start_time, stop_time)+" ms");
		
		//---- PREDICTION ----
//		if(tacticalOptimizationConfig.getUsePrediction()!=null && tacticalOptimizationConfig.getUsePrediction()) {
//			LOGGER.info("Starting prediction");
//			List<ForecastingStudy> forecastingStudies = new ArrayList<ForecastingStudy>();
//			Map<DistributionCenter, Map<Part, List<Demand>>>  group = demands.stream().collect(Collectors.groupingBy(Demand::getDistributionCenter, Collectors.groupingBy(Demand::getPart)));
//
//			group.forEach((remote, map) -> {
//				map.forEach((part,demandsList)->{
//
//					ForecastingStudy forecastingStudy = new ForecastingStudy();
//					List<DemandData> demandData = demandsList.stream().map(d->new DemandData().demandDate(new SimpleDateFormat("dd/MM/yyyy").format(d.getOrderDate())).demandValue(d.getQuantity())).collect(Collectors.toList());
//					forecastingStudy.setDemands(demandData);
//					forecastingStudy.setPart(part.getName());
//					forecastingStudy.setRemoteStation(remote.getName());
//					forecastingStudy.setFrequency((Granularity.Annual.equals(tacticalOptimizationConfig.getGranularity()))? ForecastingStudy.FrequencyEnum.A :
//						(Granularity.Monthly.equals(tacticalOptimizationConfig.getGranularity()))? ForecastingStudy.FrequencyEnum.M :
//							(Granularity.Weekly.equals(tacticalOptimizationConfig.getGranularity()))? ForecastingStudy.FrequencyEnum.W :
//								(Granularity.Daily.equals(tacticalOptimizationConfig.getGranularity()))? ForecastingStudy.FrequencyEnum.D :
//									null);
//					forecastingStudy.setHorizon(tacticalOptimizationConfig.getHorizon());
//					forecastingStudies.add(forecastingStudy);
//				});
//			});
//
//			ForecastStudyList forecastStudyList = new ForecastStudyList();
//			forecastStudyList.setStartDate(new SimpleDateFormat("dd/MM/yyyy").format(tacticalOptimizationConfig.getInitialDate()));
//			forecastStudyList.setEndDate(new SimpleDateFormat("dd/MM/yyyy").format(tacticalOptimizationConfig.getEndDate()));
//			ForecastStudyList forecastedList = forecastApi.postMulti(forecastStudyList);
//			forecastedList.getStudyList().forEach(forecastedStudy -> {
//				DistributionCenter distributionCenter = distributionCenterRepository.findByName(forecastedStudy.getRemoteStation()).get();
//				Part part = partRepository.findByName(forecastedStudy.getPart()).get();
//				List<Demand> demandsForecasted = forecastedStudy.getDemands().stream().map(dd->new Demand(null,toDate(dd.getDemandDate()), toDate(dd.getDemandDate()), part, dd.getDemandValue(),null,distributionCenter)).collect(Collectors.toList());
//				demands.addAll(demandsForecasted);
//			});
//			LOGGER.info("Finished prediction");
//		}
		start_time = Instant.now();
		PMedian model = new PMedian(tacticalOptimization, distributionCenters, distributionCentersNames, remoteStations, remoteStationsNames, parts, partsNames, processingTime, numberOfOrderByPart, demandByPart, matrix, sramCapacity, horizon);
		TacticalOptimizationResponse tacticalOptimizationResponse =null;
		if(tacticalOptimization.getType().equals(TacticalOptimizationType.CostBenefit)) {
			tacticalOptimizationResponse = model.costBenefit();
		}else if(tacticalOptimization.getType().equals(TacticalOptimizationType.NumberOfFacilites)) {
			tacticalOptimizationResponse = model.solve(tacticalOptimization.getMaximumLocations());
		}else if(tacticalOptimization.getType().equals(TacticalOptimizationType.Analysis)) {
			tacticalOptimizationResponse = model.analysis();
		}
		stop_time = Instant.now();
		LOGGER.info("Optimization time:"+ChronoUnit.MILLIS.between(start_time, stop_time)+" ms");
		
		LOGGER.info("Saving optimization results do database");
		start_time = Instant.now();
		tacticalOptimizationResultRepository.deleteByStudy(tacticalOptimization);
		if(tacticalOptimization.getType().equals(TacticalOptimizationType.CostBenefit)) {
			TacticalOptimizationResult tacticalOptimizationResult = tacticalOptimizationResponse.getTacticalOptimizationResult();
			tacticalOptimizationResultRepository.save(tacticalOptimizationResult);
		}else if(tacticalOptimization.getType().equals(TacticalOptimizationType.NumberOfFacilites)) {
			TacticalOptimizationResult tacticalOptimizationResult = tacticalOptimizationResponse.getTacticalOptimizationResult();
			tacticalOptimizationResultRepository.save(tacticalOptimizationResult);
		}else if(tacticalOptimization.getType().equals(TacticalOptimizationType.Analysis)) {
			tacticalOptimizationResponse.getTacticalOptimizationResults().forEach(tacticalOptimizationResult->{
				tacticalOptimizationResultRepository.save(tacticalOptimizationResult);
			});
		}
		stop_time = Instant.now();
		LOGGER.info("Persistence time:"+ChronoUnit.MILLIS.between(start_time, stop_time)+" ms");
		
		
		if(usermail!=null) {
			LOGGER.info("Sending email");
			Email email = new Email();
			email.setContent("Execution of Tactical Optimization study: "+ tacticalOptimization.getName() +" finished successifully");
			email.setHtml(true);
			email.setSubject("Study finished");
			email.setTo(usermail);
			try {
				emailControllerApi.sendWithFreemarkerUsingPOST(email);
			} catch (com.fasten.wp4.email.client.invoker.ApiException e) {
				LOGGER.error("Could not notify conclusion of tactical optimization study execution by email");
			}
		}
		
		return ResponseEntity.ok(tacticalOptimizationResponse);
	}

}
