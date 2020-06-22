package com.fasten.wp4.iot.fiware.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasten.wp4.database.model.Part;
import com.fasten.wp4.database.model.ProcessingPart;
import com.fasten.wp4.database.model.SRAM;
import com.fasten.wp4.database.model.SRAMCapabilities;
import com.fasten.wp4.database.model.SRAMEnviromentalInfo;
import com.fasten.wp4.database.model.SRAMProcessStatus;
import com.fasten.wp4.database.model.SRAMStatus;
import com.fasten.wp4.database.repository.PartRepository;
import com.fasten.wp4.database.repository.ProcessingPartRepository;
import com.fasten.wp4.database.repository.SRAMRepository;
import com.fasten.wp4.fpsot.frontend.client.api.FrontendNotifyControllerApi;
import com.fasten.wp4.fpsot.frontend.client.invoker.ApiException;
import com.fasten.wp4.orion.client.api.ApiEntryPointApi;
import com.fasten.wp4.orion.client.model.Model3Dprinter1Extruder;
import com.fasten.wp4.orion.client.model.Model3Dprinter1ExtruderResponse;
import com.fasten.wp4.orion.client.model.Printer3d;
import com.fasten.wp4.orion.client.model.Printer3dResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import io.swagger.annotations.ApiOperation;

@RestController
public class OrionController {
	
	private static final Logger logger = LoggerFactory.getLogger(OrionController.class);
	
	public static final String SRAM_PATH = "/webhook/sram";
	
	@Autowired
	FrontendNotifyControllerApi frontendNotifyControllerApi;
	
	@Autowired
	private ApiEntryPointApi apiEntryPointApi;
	
	@Autowired
	private SRAMRepository sramRepository;

	@Autowired
	private PartRepository partRepository;

	@Autowired
	private ProcessingPartRepository processingPartRepository;

	@ApiOperation(value = "Receive the notification from ORION", nickname = "receiveSRAMs")
	@RequestMapping(value = SRAM_PATH+"/{type}", method = RequestMethod.POST)
	public ResponseEntity<Void> receiveSRAMs(@PathVariable String type,@RequestBody Map<String, Object> payload ) throws ApiException, JsonParseException, JsonMappingException, IOException{
		
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(payload);
		//TODO testar se e o mesmo resultado acima
		//String json = apiEntryPointApi.getApiClient().getJSON().serialize(payload);
		
		logger.info("Received from orion:\n" + json);
		
		
		if("printer3d".equals(type)) {
			Printer3dResponse response = apiEntryPointApi.getApiClient().getJSON().deserialize(json, new TypeToken<Printer3dResponse>(){}.getType());
			List<Printer3d> printers = response.getData();
			logger.info("Deserealized to:\n" + response);
			
			logger.info("Start import");
			printers.forEach(p->importar(p));
			logger.info("End import");
			
			logger.info("Notifying front end");
//			frontendNotifyControllerApi.kafkaTopicNameGet("srams");
			
		}else if("3Dprinter_1_extruder".equals(type)) {

			Model3Dprinter1ExtruderResponse response = apiEntryPointApi.getApiClient().getJSON().deserialize(json, new TypeToken<Model3Dprinter1ExtruderResponse>(){}.getType());
			List<Model3Dprinter1Extruder> printers = response.getData();
			logger.info("Deserealized to:\n" + response);
			
			logger.info("Start import");
			printers.forEach(p->importar(p));
			logger.info("End import");
			
			logger.info("Notifying front end");
//			frontendNotifyControllerApi.kafkaTopicNameGet("srams");
		}
		
		
		
		return ResponseEntity.ok().build();
	}
	

	@Transactional
	private void importar(Model3Dprinter1Extruder printer3d) {
		Optional<SRAM> sramOpt = sramRepository.findByCode(printer3d.getId());
		SRAM sram=null;
		if(!sramOpt.isPresent()) {
			sram=new SRAM();
		}else {
			sram=sramOpt.get();
		}

		sram.setCode(printer3d.getId());
		sram.setPrintTime(printer3d.getTimeElapsed()!=null?Double.valueOf(printer3d.getTimeElapsed()):null);
		sram.setPrintTimeLeft(printer3d.getTimeLeft()!=null?Double.valueOf(printer3d.getTimeLeft()):null);
		sram.setProgressCompletion(printer3d.getProgress());
		sram.setPrintTimeLeftOrigin(printer3d.getPrintTimeLeftOrigin());

		SRAMEnviromentalInfo sramEnviromentalInfo = new SRAMEnviromentalInfo();
		sramEnviromentalInfo.setHumidity(printer3d.getRoomHumidity());
		sramEnviromentalInfo.setTemperature(printer3d.getRoomTemperature());
		sram.setEnviromentalInfo(sramEnviromentalInfo);

		if(printer3d.getJobFilamentLength()!=null || printer3d.getJobFilamentVolume()!=null) {
			SRAMCapabilities sramCapabilities = new SRAMCapabilities();
			if(printer3d.getJobFilamentVolume()!=null ) {
				sramCapabilities.setVolume(printer3d.getJobFilamentVolume());
			}
			if(printer3d.getJobFilamentLength()!=null) {
				sramCapabilities.setLenght(printer3d.getJobFilamentLength());
			}
			sram.setCapabilities(sramCapabilities);
		}

		if(printer3d.getJobFileName()!=null) {
			Optional<Part> partOptional = partRepository.findByLayout(printer3d.getJobFileName());
			if(partOptional.isPresent()) {
				Part part = partOptional.get();
				Optional<ProcessingPart> processingPartOptional = processingPartRepository.findByPartCodeAndSRAMCode(part.getCode(), sram.getCode());
				ProcessingPart processingPart=null;
				if(processingPartOptional.isPresent()) {
					processingPart=processingPartOptional.get();
					processingPart.setAveragePrintTime(printer3d.getAveragePrintTime());
					processingPart.setEstimatedPrintTime(printer3d.getEstimatedPrintTime());
					processingPart.setLastPrintTime(printer3d.getLastPrintTime());
					processingPart.setSRAM(sram);
					processingPart.setPart(part);
				}else {
					processingPart= new ProcessingPart();
					processingPart.setAveragePrintTime(printer3d.getAveragePrintTime());
					processingPart.setEstimatedPrintTime(printer3d.getEstimatedPrintTime());
					processingPart.setLastPrintTime(printer3d.getLastPrintTime());
					processingPart.setSRAM(sram);
					processingPart.setPart(part);
				}
				processingPartRepository.save(processingPart);
			}
		}

		//From http://docs.octoprint.org/en/master/api/job.html
		//"Bed heating", "Operational", "Printing", "Pausing", "Paused", "Cancelling", "Error", "Offline", "Finishing", ""
		switch(printer3d.getPrinter3dState()) {
		case "Operational":
			sram.setProcessStatus(SRAMProcessStatus.Standby);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Bed heating":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Printing":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Pausing":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Paused":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Cancelling":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Finishing":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Error":
			sram.setProcessStatus(SRAMProcessStatus.Error);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Offline":
			sram.setProcessStatus(SRAMProcessStatus.NoInfo);
			sram.setStatus(SRAMStatus.Offline);
			break;
		}

		//Temp: Não pode ser menor que 30 graus
		//Umidade: Entre 40-50 %
		if(!(sramEnviromentalInfo.getTemperature().compareTo(30d)>=0 && 
				(sramEnviromentalInfo.getHumidity().compareTo(40d)>=0 && sramEnviromentalInfo.getHumidity().compareTo(50d)<=0)
				)) {
			sram.setProcessStatus(SRAMProcessStatus.NotReady);
		}

		sramRepository.save(sram);
	}
	
	
	@Transactional
	private void importar(Printer3d printer3d) {
		Optional<SRAM> sramOpt = sramRepository.findByCode(printer3d.getId());
		SRAM sram=null;
		if(!sramOpt.isPresent()) {
			sram=new SRAM();
		}else {
			sram=sramOpt.get();
		}

		sram.setCode(printer3d.getId());
		sram.setPrintTime(printer3d.getTimeElapsed()!=null?Double.valueOf(printer3d.getTimeElapsed()):null);
		sram.setPrintTimeLeft(printer3d.getTimeLeft()!=null?Double.valueOf(printer3d.getTimeLeft()):null);
		sram.setProgressCompletion(printer3d.getProgress()!=null?printer3d.getProgress().doubleValue():null);

//		SRAMEnviromentalInfo sramEnviromentalInfo = new SRAMEnviromentalInfo();
//		sramEnviromentalInfo.setHumidity(null);
//		sramEnviromentalInfo.setTemperature(null);
//		sram.setEnviromentalInfo(sramEnviromentalInfo);

//		if(printer3d.getJobFilamentLength()!=null || printer3d.getJobFilamentVolume()!=null) {
//			SRAMCapabilities sramCapabilities = new SRAMCapabilities();
//			if(printer3d.getJobFilamentVolume()!=null ) {
//				sramCapabilities.setVolume(printer3d.getJobFilamentVolume());
//			}
//			if(printer3d.getJobFilamentLength()!=null) {
//				sramCapabilities.setLenght(printer3d.getJobFilamentLength());
//			}
//			sram.setCapabilities(sramCapabilities);
//		}

//		if(printer3d.getFileName()!=null) {
//			Optional<Part> partOptional = partRepository.findByLayout(printer3d.getFileName());
//			if(partOptional.isPresent()) {
//				Part part = partOptional.get();
//				Optional<ProcessingPart> processingPartOptional = processingPartRepository.findByPartCodeAndSRAMCode(part.getCode(), sram.getCode());
//				ProcessingPart processingPart=null;
//				if(processingPartOptional.isPresent()) {
//					processingPart=processingPartOptional.get();
//					processingPart.setAveragePrintTime(printer3d.getAveragePrintTime());
//					processingPart.setEstimatedPrintTime(printer3d.getEstimatedPrintTime());
//					processingPart.setLastPrintTime(printer3d.getLastPrintTime());
//					processingPart.setSRAM(sram);
//					processingPart.setPart(part);
//				}else {
//					processingPart= new ProcessingPart();
//					processingPart.setAveragePrintTime(printer3d.getAveragePrintTime());
//					processingPart.setEstimatedPrintTime(printer3d.getEstimatedPrintTime());
//					processingPart.setLastPrintTime(printer3d.getLastPrintTime());
//					processingPart.setSRAM(sram);
//					processingPart.setPart(part);
//				}
//				processingPartRepository.save(processingPart);
//			}
//		}
		
		//From http://docs.octoprint.org/en/master/api/job.html
		//"Bed heating", "Operational", "Printing", "Pausing", "Paused", "Cancelling", "Error", "Offline", "Finishing", ""
		switch(printer3d.getPrinter3dState()) {
		case "Operational":
			sram.setProcessStatus(SRAMProcessStatus.Standby);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Bed heating":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Printing":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Pausing":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Paused":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Cancelling":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Finishing":
			sram.setProcessStatus(SRAMProcessStatus.InOperation);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Error":
			sram.setProcessStatus(SRAMProcessStatus.Error);
			sram.setStatus(SRAMStatus.Online);
			break;
		case "Offline":
			sram.setProcessStatus(SRAMProcessStatus.NoInfo);
			sram.setStatus(SRAMStatus.Offline);
			break;
		}

		//Temp: Não pode ser menor que 30 graus
		//Umidade: Entre 40-50 %
//		if(!(sramEnviromentalInfo.getTemperature().compareTo(30d)>=0 && 
//				(sramEnviromentalInfo.getHumidity().compareTo(40d)>=0 && sramEnviromentalInfo.getHumidity().compareTo(50d)<=0)
//				)) {
//			sram.setProcessStatus(SRAMProcessStatus.NotReady);
//		}

		sramRepository.save(sram);
	}
}


