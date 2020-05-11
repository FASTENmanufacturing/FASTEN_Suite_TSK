package com.fasten.wp4.iot.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasten.wp4.iot.kafka.publisher.ReturnHistoricalTimesEventPublisher;
import com.fasten.wp4.iot.kafka.request.HistoricalTimesRequest;
import com.fasten.wp4.iot.kafka.response.HistoricalTimesResponse;
import com.fasten.wp4.iot.kafka.service.EmulateDataService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GetHistoricalTimesEventConsumer {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	EmulateDataService emulateDataService;
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	ReturnHistoricalTimesEventPublisher returnHistoricalTimesPublisher;

	@KafkaListener(id="get_historical_times_kafka_listner", topics = "get_historical_times")
	public void receive(Object requestObj, @Payload(required=false) String json,@Header("action") String action) throws Exception {
		
		LOGGER.info("KAFKA ---> Deserialize get_historical_times event");
		HistoricalTimesRequest request = mapper.readValue(json,new TypeReference<HistoricalTimesRequest>() {});

		LOGGER.info("EMULATE");
		HistoricalTimesResponse historicalTimesResponse = emulateDataService.emulateHistoricalTimesResponseData(request);
		
		returnHistoricalTimesPublisher.publish(historicalTimesResponse);
	}

}
