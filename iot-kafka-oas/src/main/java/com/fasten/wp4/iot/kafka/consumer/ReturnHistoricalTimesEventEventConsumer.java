package com.fasten.wp4.iot.kafka.consumer;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.fpsot.frontend.client.api.FrontendNotifyControllerApi;
import com.fasten.wp4.iot.kafka.response.HistoricalTimesResponse;
import com.fasten.wp4.iot.kafka.service.EmulatedImportDataService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ReturnHistoricalTimesEventEventConsumer {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	EmulatedImportDataService emulatedImportDataService;
	
	@Autowired
	FrontendNotifyControllerApi frontendNotifyControllerApi;

	@KafkaListener(id="return_historical_times_kafka_listner",topics = "return_historical_times")
	public void receive(Object requestObj, @Payload(required=false) String json,@Header("action") String action) throws Exception {

		LOGGER.info("KAFKA ---> Deserialize return_historical_times event");
		
		HistoricalTimesResponse response = mapper.readValue(json,new TypeReference<HistoricalTimesResponse>() {});

		LOGGER.info("IMPORT");
//		try {
//			if(false)
//			emulatedImportDataService.updateHistoricalTimesData(response);
//		} catch (ApiException | URISyntaxException e) {
//			e.printStackTrace();
//		}
		LOGGER.info("END of IMPORT");
		
		
		LOGGER.info("send REST to FPSOT");
		//asyncNotifyMB.sendPushMessage("return_historical_times");
		frontendNotifyControllerApi.kafkaTopicNameGet("return_historical_times");
		

	}

}
