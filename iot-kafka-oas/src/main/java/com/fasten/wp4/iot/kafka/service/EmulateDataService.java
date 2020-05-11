package com.fasten.wp4.iot.kafka.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasten.wp4.database.client.api.DeliveryControllerApi;
import com.fasten.wp4.database.client.api.DemandControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.ProcessingPartControllerApi;
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.api.SramControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.ProcessingPart;
import com.fasten.wp4.iot.kafka.request.HistoricalTimesRequest;
import com.fasten.wp4.iot.kafka.request.ProbabilityDistribuitionsRequest;
import com.fasten.wp4.iot.kafka.response.HistoricalDemandResponse;
import com.fasten.wp4.iot.kafka.response.HistoricalTimesResponse;
import com.fasten.wp4.iot.kafka.response.ProbabilityDristribuitionsResponse;

@Service
public class EmulateDataService{

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	DemandControllerApi demandControllerApi;
	@Autowired
	DeliveryControllerApi deliveryControllerApi;
	@Autowired
	PartControllerApi partControllerApi;
	@Autowired
	SramControllerApi sramControllerApi;
	@Autowired
	ProcessingPartControllerApi processingPartControllerApi;
	@Autowired
	RemoteStationControllerApi remoteStationControllerApi;


	public HistoricalDemandResponse emulateHistoricalDemandResponseData(Date initialDate, Date endDate) {

		LOGGER.debug("emulating data");

		HistoricalDemandResponse historicalDemandResponse = new HistoricalDemandResponse();
		historicalDemandResponse.setInitialDate(initialDate);
		historicalDemandResponse.setEndDate(endDate);
		try {
			historicalDemandResponse.setDeliveries(deliveryControllerApi.retrieveAllDelivery());
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			String startDateStr = sdf.format(initialDate);
			String endDateStr =sdf.format(endDate);
			historicalDemandResponse.setDemands(demandControllerApi.retrieveBetween(endDateStr, startDateStr));
			historicalDemandResponse.setParts(partControllerApi.retrieveAllPart());
			historicalDemandResponse.setSRAMs(sramControllerApi.retrieveAllSRAM());
			historicalDemandResponse.setProcessingParts(processingPartControllerApi.retrieveAllProcessingPart());
			historicalDemandResponse.setRemoteStations(remoteStationControllerApi.retrieveAllRemoteStation());
		} catch (ApiException e) {
			e.printStackTrace();
		}

		return historicalDemandResponse;
	}

	public ProbabilityDristribuitionsResponse emulateProbabilityDistribuitionsResponseData(ProbabilityDistribuitionsRequest probabilityDistribuitionsRequest) {

		ProbabilityDristribuitionsResponse probabilityDristribuitionsResponse = new ProbabilityDristribuitionsResponse();
		List<ProcessingPart> processingParts = null;
		try {
			processingParts = processingPartControllerApi.retrieveAllProcessingPart();
		} catch (ApiException e) {
			e.printStackTrace();
		}

		probabilityDristribuitionsResponse.setProcessingParts(processingParts);

//		for (ProcessingPart processingPart : processingParts) {
//			switch ((int)(Math.ceil(Math.random()*10)%2)) {
//			case 0:
//				processingPart.setAvgProducingTime(processingPart.getAvgProducingTime()+processingPart.getStdProducingTime());
//				processingPart.setAvgSetupTime(processingPart.getAvgSetupTime()+processingPart.getStdSetupTime());
//				break;
//			case 1:
//				processingPart.setAvgProducingTime(processingPart.getAvgProducingTime()-processingPart.getStdProducingTime());
//				processingPart.setAvgSetupTime(processingPart.getAvgSetupTime()-processingPart.getStdSetupTime());
//				break;
//			}
//		}
		return probabilityDristribuitionsResponse;
	}
	
	
	public HistoricalTimesResponse emulateHistoricalTimesResponseData(HistoricalTimesRequest request) {

		HistoricalTimesResponse historicalTimesResponse = new HistoricalTimesResponse();
		List<ProcessingPart> processingParts = null;
		try {
			processingParts = processingPartControllerApi.retrieveAllProcessingPart();
		} catch (ApiException e) {
			e.printStackTrace();
		}

		historicalTimesResponse.setProcessingParts(processingParts);

//		for (ProcessingPart processingPart : processingParts) {
//			switch ((int)(Math.ceil(Math.random()*10)%2)) {
//			case 0:
//				processingPart.setAvgProducingTime(processingPart.getAvgProducingTime()+processingPart.getStdProducingTime());
//				processingPart.setAvgSetupTime(processingPart.getAvgSetupTime()+processingPart.getStdSetupTime());
//				break;
//			case 1:
//				processingPart.setAvgProducingTime(processingPart.getAvgProducingTime()-processingPart.getStdProducingTime());
//				processingPart.setAvgSetupTime(processingPart.getAvgSetupTime()-processingPart.getStdSetupTime());
//				break;
//			}
//		}
		return historicalTimesResponse;
	}


}
