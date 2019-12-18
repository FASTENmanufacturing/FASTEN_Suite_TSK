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

import com.fasten.wp4.iot.kafka.response.ProbabilityDristribuitionsResponse;


@Component
public class ReturnProbabilityDistribuitionsEventPublisher{

	@Autowired 
	private KafkaTemplate<String, ProbabilityDristribuitionsResponse> kafkaTemplate;
	
	@Autowired
	private NewTopic returnProbabilityDistribuitionsTopic;
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	public void publish(ProbabilityDristribuitionsResponse response) {
		LOGGER.info("Publishing return_probability_distribuitions ---> KAFKA");
		ProducerRecord<String, ProbabilityDristribuitionsResponse> producerRecord = new ProducerRecord<>(
				returnProbabilityDistribuitionsTopic.name(), 					//name of topic
				0,  													//partition 0 for registration
				System.currentTimeMillis(), 							//time in millis
				"0", 													//key
				response, 													//the event
				Arrays.asList(new RecordHeader("action", returnProbabilityDistribuitionsTopic.name().getBytes()))); 	//event done as header

		kafkaTemplate.send(producerRecord);
	}

}
