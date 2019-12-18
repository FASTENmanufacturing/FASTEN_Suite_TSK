package com.fasten.wp4.iot.task;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasten.wp4.iot.kafka.publisher.GetHistoricalTimesEventPublisher;
import com.fasten.wp4.iot.kafka.request.HistoricalTimesRequest;
import com.fasten.wp4.iot.model.Producer;
import com.fasten.wp4.iot.repository.ProducerRepository;

@Service
public class ScheduledFireGetHistoricalTimesEvent  implements ScheduledFireEvent {
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GetHistoricalTimesEventPublisher getHistoricalTimesEventPublisher;
	
	@Autowired
	ProducerRepository producerRepository;
	
	@Override
	public String getTopic() {
		return "get_historical_times";
	}

	@Override
	public String getName() {
		return "get_historical_times_kafka_producer";
	}
	

	@Scheduled(fixedRate=60000)
	public void runsEveryMinute() {
		Optional<Producer> producer = producerRepository.findByName(getName());
		if(producer.isPresent() && producer.get().getEnabled()) {
			HistoricalTimesRequest request = new HistoricalTimesRequest();
			getHistoricalTimesEventPublisher.publish(request);
			
		}else {
			LOGGER.info("Ignoring Automatic Publishing get_historical_times ");
		}
			
    }
	
}
