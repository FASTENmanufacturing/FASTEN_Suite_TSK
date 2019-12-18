package com.fasten.wp4.iot.task;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasten.wp4.iot.kafka.publisher.GetHistoricalDemandEventPublisher;
import com.fasten.wp4.iot.kafka.request.HistoricalDemandRequest;
import com.fasten.wp4.iot.model.Producer;
import com.fasten.wp4.iot.repository.ProducerRepository;

@Service
public class ScheduledFireGetHistoricalDemandEvent implements ScheduledFireEvent {
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ProducerRepository producerRepository;
	
	@Autowired
	private GetHistoricalDemandEventPublisher getHistoricalDemandEventPublisher;
	
	@Override
	public String getTopic() {
		return "get_historical_demand";
	}

	@Override
	public String getName() {
		return "get_historical_demand_kafka_producer";
	}
	
	@Scheduled(fixedRate=60000)
	 public void run() {
		Optional<Producer> producer = producerRepository.findByName(getName());
		if(producer.isPresent() && producer.get().getEnabled()) {
		
			HistoricalDemandRequest request = new HistoricalDemandRequest();
			Date initialDate = new GregorianCalendar(2017, 01, 26).getTime();
			Date endDate = new GregorianCalendar(2017, 03, 28).getTime();
			request.setInitialDate(initialDate);
			request.setEndDate(endDate);
		
			getHistoricalDemandEventPublisher.publish(request);
			
		}else {
			LOGGER.info("Ignoring Automatic Publishing get_historical_demand ");
		}
			
    }
	
}
