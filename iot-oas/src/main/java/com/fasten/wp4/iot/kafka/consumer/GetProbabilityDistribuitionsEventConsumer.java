package com.fasten.wp4.iot.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasten.wp4.iot.kafka.publisher.ReturnProbabilityDistribuitionsEventPublisher;
import com.fasten.wp4.iot.kafka.request.ProbabilityDistribuitionsRequest;
import com.fasten.wp4.iot.kafka.response.ProbabilityDristribuitionsResponse;
import com.fasten.wp4.iot.service.EmulateDataService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GetProbabilityDistribuitionsEventConsumer {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	EmulateDataService emulateDataService;
	
	@Autowired
	ReturnProbabilityDistribuitionsEventPublisher returnProbabilityDistribuitionsPublisher;

	@KafkaListener(id="get_probability_distribuitions_kafka_listner", topics = "get_probability_distribuitions")
	public void receive(Object requestObj, @Payload(required=false) String json,@Header("action") String action) throws Exception {
		LOGGER.info("KAFKA ---> Deserialize get_probability_distribuitions");
		ProbabilityDistribuitionsRequest probabilityDistribuitionsRequest = mapper.readValue(json,new TypeReference<ProbabilityDistribuitionsRequest>() {});

		LOGGER.info("EMULATE");
		ProbabilityDristribuitionsResponse probabilityDristribuitionsResponse = emulateDataService.emulateProbabilityDistribuitionsResponseData(probabilityDistribuitionsRequest);
		
		returnProbabilityDistribuitionsPublisher.publish(probabilityDristribuitionsResponse);
	}

}
