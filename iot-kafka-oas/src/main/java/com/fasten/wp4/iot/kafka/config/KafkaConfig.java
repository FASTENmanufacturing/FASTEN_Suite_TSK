package com.fasten.wp4.iot.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class KafkaConfig extends KafkaAutoConfiguration{
	
//	@Autowired
//	KafkaUtils kafkaUtils;
	
	@Value("${spring.kafka.replyingtemplate.timeout}")
	private Long springKafkaReplyingTemplateTimeout;
	
	private final KafkaProperties properties;

	private final RecordMessageConverter messageConverter;

	public KafkaConfig(KafkaProperties properties, ObjectProvider<RecordMessageConverter> messageConverter) {
		super(properties, messageConverter);
		this.properties=properties;
		this.messageConverter=messageConverter.getIfUnique();
	}

	//same as saying that consumers may scale to 5 factor as one to one then more then one partition by consumer
	int defaultPartitionNumber = 1;
	
	@Bean
	public NewTopic getHistoricalDemandTopic() {
//		kafkaUtils.creteTopicIfNonExists("get_historical_demand");
		return new NewTopic("get_historical_demand", defaultPartitionNumber, (short) 1);
	}
	
	@Bean
	public NewTopic returnHistoricalDemandTopic() {
//		kafkaUtils.creteTopicIfNonExists("return_historical_demand");
		return new NewTopic("return_historical_demand", defaultPartitionNumber, (short) 1);
	}
	
	@Bean
	public NewTopic getHistoricalTimesTopic() {
//		kafkaUtils.creteTopicIfNonExists("get_historical_demand");
		return new NewTopic("get_historical_times", defaultPartitionNumber, (short) 1);
	}
	
	@Bean
	public NewTopic returnHistoricalTimesTopic() {
//		kafkaUtils.creteTopicIfNonExists("return_historical_demand");
		return new NewTopic("return_historical_times", defaultPartitionNumber, (short) 1);
	}
	
	
	@Bean
	public NewTopic getProbabilityDistribuitionsTopic() {
//		kafkaUtils.creteTopicIfNonExists("get_probability_distribuitions");
		return new NewTopic("get_probability_distribuitions", defaultPartitionNumber, (short) 1);
	}
	
	@Bean
	public NewTopic returnProbabilityDistribuitionsTopic() {
//		kafkaUtils.creteTopicIfNonExists("return_probability_distribuitions");
		return new NewTopic("return_probability_distribuitions", defaultPartitionNumber, (short) 1);
	}
	
	@Bean 
	public ObjectMapper objectMapper() {
		return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
								 .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	@Bean
	public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory,ProducerListener<Object, Object> kafkaProducerListener) {
		
		KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
		if (this.messageConverter != null) {
			kafkaTemplate.setMessageConverter(this.messageConverter);
		}
		kafkaTemplate.setProducerListener(kafkaProducerListener);
		kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
		return kafkaTemplate;
	}

//	@Bean
//	public ConcurrentMessageListenerContainer<Object, Object> responseContainer(ConcurrentKafkaListenerContainerFactory<Object, Object> containerFactory) {
//		ConcurrentMessageListenerContainer<Object, Object> responseContainer = containerFactory.createContainer(responseTopic().name());
//		responseContainer.getContainerProperties().setGroupId(this.properties.getConsumer().getGroupId());
//		responseContainer.setAutoStartup(false);
//		return responseContainer;
//	}

//	@Bean
//	public ReplyingKafkaTemplate<?, ?, ?> replyingTemplate(ProducerFactory<Object, Object> kafkaProducerFactory, ConcurrentMessageListenerContainer<Object, Object> responseContainer) {
//		ReplyingKafkaTemplate<Object, Object,Object> replyingKafkaTemplate = new ReplyingKafkaTemplate<Object, Object,Object>(kafkaProducerFactory, responseContainer);
//		replyingKafkaTemplate.setReplyTimeout(springKafkaReplyingTemplateTimeout);
//		replyingKafkaTemplate.setSharedReplyTopic(true);
//		return replyingKafkaTemplate;
//	}

}
