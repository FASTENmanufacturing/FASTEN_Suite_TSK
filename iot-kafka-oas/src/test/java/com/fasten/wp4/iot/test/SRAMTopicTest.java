//package com.fasten.wp4.test;
//
//import java.io.IOException;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.fasten.wp4.kafka.publisher.ReturnHistoricalDemandEventPublisher;
//import com.fasten.wp4.kafka.request.HistoricalDemandRequest;
//import com.fasten.wp4.kafka.response.HistoricalDemandResponse;
//import com.fasten.wp4.model.TopicSRAM;
//import com.fasten.wp4.service.EmulateDataService;
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//
////@Ignore
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class SRAMTopicTest {
//	
//	private Logger LOGGER = LoggerFactory.getLogger(getClass());
//
//	@Autowired
//	EmulateDataService emulateDataService;
//	
//	@Autowired
//	ObjectMapper mapper;
//	
//	@KafkaListener(id="status-topic2_listner", topics = "status-topic2")
//	public void receive(Object requestObj, @Payload(required=false) String json,@Header("action") String action) throws Exception {
//		
//		LOGGER.info("KAFKA ---> Deserialize status-topic2");
//		TopicSRAM sram = mapper.readValue(json,new TypeReference<TopicSRAM>() {});
//		sram.getEnviromentalInfo().getHumidity();
//		System.out.println("fim");
//	}
//
//}
