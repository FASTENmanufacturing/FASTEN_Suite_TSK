package com.fasten.wp4.iot.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasten.wp4.iot.kafka.publisher.ReturnHistoricalDemandEventPublisher;
import com.fasten.wp4.iot.kafka.request.HistoricalDemandRequest;
import com.fasten.wp4.iot.kafka.response.HistoricalDemandResponse;
import com.fasten.wp4.iot.service.EmulateDataService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GetHistoricalDemandEventConsumer {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	EmulateDataService emulateDataService;
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	ReturnHistoricalDemandEventPublisher publisher;
	
	@KafkaListener(id="get_historical_demand_kafka_listner", topics = "get_historical_demand")
	public void receive(Object requestObj, @Payload(required=false) String json,@Header("action") String action) throws Exception {
		
		LOGGER.info("KAFKA ---> Deserialize get_historical_demand");
		HistoricalDemandRequest request = mapper.readValue(json,new TypeReference<HistoricalDemandRequest>() {});
		
		LOGGER.info("EMULATE");
		HistoricalDemandResponse response = emulateDataService.emulateHistoricalDemandResponseData(request.getInitialDate(), request.getEndDate());
		
		publisher.publish(response);
	}

}
