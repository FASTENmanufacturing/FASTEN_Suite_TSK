package com.fasten.wp4.iot.kafka.publisher;

import java.util.Arrays;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasten.wp4.iot.kafka.request.ProbabilityDistribuitionsRequest;


@Component
public class GetProbabilityDistribuitionsEventPublisher{

	@Autowired 
	private KafkaTemplate<String, ProbabilityDistribuitionsRequest> kafkaTemplate;
	
	@Autowired
	private NewTopic getProbabilityDistribuitionsTopic;
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	public void publish(ProbabilityDistribuitionsRequest request) {
		LOGGER.info("Publishing get_probability_distribuitions ---> KAFKA");
		ProducerRecord<String, ProbabilityDistribuitionsRequest> producerRecord = new ProducerRecord<>(
				getProbabilityDistribuitionsTopic.name(), 				//name of topic
				0,  													//partition 0 for registration
				System.currentTimeMillis(), 							//time in millis
				"0", 													//key
				request, 													//the event
				Arrays.asList(new RecordHeader("action", getProbabilityDistribuitionsTopic.name().getBytes()))); 	//event done as header

		kafkaTemplate.send(producerRecord);
		
	}

}
