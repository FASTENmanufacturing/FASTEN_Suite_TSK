package com.fasten.wp4.iot.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.fpsot.frontend.client.api.FrontendNotifyControllerApi;
import com.fasten.wp4.iot.kafka.response.HistoricalDemandResponse;
import com.fasten.wp4.iot.kafka.service.EmulatedImportDataService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ReturnHistoricalDemandEventConsumer {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	EmulatedImportDataService emulatedImportDataService;
	
	@Autowired
	FrontendNotifyControllerApi frontendNotifyControllerApi;

	@KafkaListener(id="return_historical_demand_kafka_listner",topics = "return_historical_demand")
	public void receive(Object requestObj, @Payload(required=false) String json,@Header("action") String action) throws Exception {

		LOGGER.info("KAFKA ---> Deserialize return_historical_demand event");
		
		HistoricalDemandResponse response = mapper.readValue(json,new TypeReference<HistoricalDemandResponse>() {});
		LOGGER.info("IMPORT");
//		try {
//			emulatedImportDataService.importData(response);
//		} catch (ApiException e) {
//			e.printStackTrace();
//		}
		LOGGER.info("END of import");
		
		//TODO send REST to FPSOT
		LOGGER.info("send REST to FPSOT");
		
		//asyncNotifyMB.sendPushMessage("return_historical_demand");
		frontendNotifyControllerApi.kafkaTopicNameGet("return_historical_demand");
	}

}
