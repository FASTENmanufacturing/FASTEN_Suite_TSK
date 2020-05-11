package com.fasten.wp4.iot.kafka.task;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasten.wp4.iot.kafka.model.Producer;
import com.fasten.wp4.iot.kafka.publisher.GetProbabilityDistribuitionsEventPublisher;
import com.fasten.wp4.iot.kafka.repository.ProducerRepository;
import com.fasten.wp4.iot.kafka.request.ProbabilityDistribuitionsRequest;

@Service
public class ScheduledFireGetProbabilityDistribuitionsEvent  implements ScheduledFireEvent {
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GetProbabilityDistribuitionsEventPublisher getProbabilityDistribuitionsEventPublisher;
	
	@Autowired
	ProducerRepository producerRepository;
	
	@Override
	public String getTopic() {
		return "get_probability_distribuitions";
	}

	@Override
	public String getName() {
		return "get_probability_distribuitions_kafka_producer";
	}
	

	@Scheduled(fixedRate=60000)
	public void runsEveryMinute() {
		Optional<Producer> producer = producerRepository.findByName(getName());
		if(producer.isPresent() && producer.get().getEnabled()) {
			ProbabilityDistribuitionsRequest request = new ProbabilityDistribuitionsRequest();
			getProbabilityDistribuitionsEventPublisher.publish(request);
		}else {
			LOGGER.info("Ignoring Automatic Publishing get_probability_distribuitions ");
		}
			
    }
	
}
