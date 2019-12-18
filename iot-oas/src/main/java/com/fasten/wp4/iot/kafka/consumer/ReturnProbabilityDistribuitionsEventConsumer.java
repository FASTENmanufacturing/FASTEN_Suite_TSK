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
import com.fasten.wp4.iot.kafka.response.ProbabilityDristribuitionsResponse;
import com.fasten.wp4.iot.service.ImportDataService;
import com.fasten.wp4.kafka.response.client.api.KafkaResponseControllerApi;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ReturnProbabilityDistribuitionsEventConsumer {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	ImportDataService importDataService;
	
	@Autowired
	KafkaResponseControllerApi kafkaResponseControllerApi;

	@KafkaListener(id="return_probability_distribuitions_kafka_listner",topics = "return_probability_distribuitions")
	public void receive(Object requestObj, @Payload(required=false) String json,@Header("action") String action) throws Exception {

		LOGGER.info("KAFKA ---> Deserialize return_probability_distribuitions");
		
		ProbabilityDristribuitionsResponse response = mapper.readValue(json,new TypeReference<ProbabilityDristribuitionsResponse>() {});

		LOGGER.info("IMPORT");
//		try {
//			importDataService.updateProbabilityDistribuitionsData(response);
//		} catch (ApiException | URISyntaxException e) {
//			e.printStackTrace();
//		}
		LOGGER.info("END of IMPORT");
		
		LOGGER.info("send REST to FHSOT");
		//asyncNotifyMB.sendPushMessage("return_probability_distribuitions");
		kafkaResponseControllerApi.kafkaTopicNameGet("return_probability_distribuitions");

	}

}
