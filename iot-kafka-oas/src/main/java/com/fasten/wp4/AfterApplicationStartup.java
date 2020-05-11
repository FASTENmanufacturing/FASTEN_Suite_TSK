package com.fasten.wp4;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.scheduling.config.IntervalTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Component;

import com.fasten.wp4.iot.kafka.config.KafkaUtils;
import com.fasten.wp4.iot.kafka.config.SchedulerConfig;
import com.fasten.wp4.iot.kafka.model.Listner;
import com.fasten.wp4.iot.kafka.model.Producer;
import com.fasten.wp4.iot.kafka.model.Topic;
import com.fasten.wp4.iot.kafka.repository.ListnerRepository;
import com.fasten.wp4.iot.kafka.repository.ProducerRepository;
import com.fasten.wp4.iot.kafka.repository.TopicRepository;
import com.fasten.wp4.iot.kafka.task.ScheduledFireEvent;

@Component
public class AfterApplicationStartup {
	
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	KafkaUtils kafkaUtils;
	
	@Autowired
	TopicRepository topicRepository;
	
	@Autowired
	ListnerRepository listnerRepository;

	@Autowired
	private ProducerRepository producerRepository;
	
	@Autowired
	SchedulerConfig schedulerConfig;
	
	@EventListener(ApplicationReadyEvent.class)
	public void doAfterStartup() {
		
		listnerRepository.deleteAll();
		producerRepository.deleteAll();
		topicRepository.deleteAll();
		
		//para cada topico
		Set<String> listTopics = kafkaUtils.listTopics();
		List<Topic> topics = listTopics.stream().map(s-> new Topic(s,true)).collect(Collectors.toList());
		topics.forEach(t-> topicRepository.save(t));
		LOGGER.info("Reading and local saving topics");
		
		//verifico a existencia de listners
		List<Listner> listners = topics.stream().map(t->{
				Listner l = new Listner();
				MessageListenerContainer messageListner = kafkaUtils.getListnerByTopicName(t.getName());
				l.setName((messageListner!=null)?messageListner.getContainerProperties().getGroupId():null);
				l.setEnabled(true);
				l.setTopic(t);
				return l;
			}).filter(l->l.getName()!=null).collect(Collectors.toList());
		listners.forEach(l->listnerRepository.save(l));
		LOGGER.info("Detecting listners");
		
		//crio tarefas a partir do registro
		ScheduledTaskRegistrar taskRegistrar = schedulerConfig.getTaskRegistrar();
		List<IntervalTask> fixedRateTaskList = taskRegistrar.getFixedRateTaskList();
		List<Producer> producers = fixedRateTaskList.stream().map(t->{
			Producer producer = new Producer();
			producer.setInitialDelay(t.getInitialDelay());
			producer.setInterval(t.getInterval());
			ScheduledMethodRunnable smr = (ScheduledMethodRunnable) t.getRunnable();
			if (smr.getTarget() instanceof ScheduledFireEvent){
				ScheduledFireEvent task = (ScheduledFireEvent) smr.getTarget();
				producer.setName(task.getName());
				producer.setTopic(topicRepository.findByName(task.getTopic()).get());
			}
			producer.setEnabled(false);
			return producer;
		}).collect(Collectors.toList());
		producers.forEach(p->producerRepository.save(p));
		LOGGER.info("Geting registered tasks");
	
	}
	
}

//	private Serializable getEventByProducer(Producer producer) {
//		if(producer.getTopic().getName().equals("get_historical_demand")) {
//			HistoricalDemandRequest historicalDemandRequest = new HistoricalDemandRequest();
//			Date initialDate = new GregorianCalendar(2015, 06, 26).getTime();
//			Date endDate = new GregorianCalendar(2017, 06, 28).getTime();
//			historicalDemandRequest.setInitialDate(initialDate);
//			historicalDemandRequest.setEndDate(endDate);
//		
//			GetHistoricalDemandEvent event = new GetHistoricalDemandEvent(historicalDemandRequest);
//			return event;
//		}else if(producer.getTopic().getName().equals("get_probability_distribuitions")) {
//			ProbabilityDistribuitionsRequest probabilityDistribuitionsRequest = new ProbabilityDistribuitionsRequest();
//			GetProbabilityDistribuitionsEvent event = new GetProbabilityDistribuitionsEvent(probabilityDistribuitionsRequest);
//			return event;
//		}else {
//			return null;
//		}
//	}

//		producers.forEach(producer->{
//			taskRegistrar.addFixedRateTask(new IntervalTask(
//					new Runnable() {
//						@Override
//						public void run() {
//	                		if(producer.getEnabled()) {
//	                			Serializable event = getEventByProducer(producer);
//	                			LOGGER.info("Automatic Publishing "+producer.getTopic().getName()+" event to KAFKA");
//	                			ProducerRecord<String, Serializable> producerRecord = new ProducerRecord<>(
//	                					producer.getTopic().getName(), 				//name of topic
//	                					0,  													//partition 0 for registration
//	                					System.currentTimeMillis(), 							//time in millis
//	                					"0", 													//key
//	                					event, 													//the event
//	                					Arrays.asList(new RecordHeader("action", producer.getTopic().getName().getBytes()))); 	//event done as header
//
//	                			kafkaTemplate.send(producerRecord);
//	                		}else {
//	                			LOGGER.info("Ignoring Automatic Publishing "+producer.getTopic().getName());
//	                		}
//	                    }
//					}, producer.getInterval(), producer.getInitialDelay()));
//			
//		});