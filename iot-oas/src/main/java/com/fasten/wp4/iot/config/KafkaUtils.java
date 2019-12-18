package com.fasten.wp4.iot.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class KafkaUtils {

	@Autowired
	private KafkaAdmin kafkaAdmin;
	
	@Autowired
    private ConsumerFactory<String, String> consumerFactory;
	
	@Autowired
	private KafkaListenerEndpointRegistry registry;
	
	@Autowired
	private ApplicationContext appContext;
	
	KafkaAdminClient admin;
	
	@PostConstruct
    private void init() {
		admin = (KafkaAdminClient) KafkaAdminClient.create(kafkaAdmin.getConfig());
	}
	

	public void creteTopicIfNonExists(String topic) {
		try {
			ListTopicsResult listTopics = admin.listTopics();
			Set<String> names = listTopics.names().get();
			if (!names.contains(topic)) {
				admin.createTopics(Arrays.asList(new NewTopic(topic, 1, (short) 1)));
			}
			admin.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public Set<String> listTopics() {
		ListTopicsResult listTopics = admin.listTopics();
		try {
			return listTopics.names().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public NewTopic get(String name) {
		try {
			Map<String, TopicListing> map = admin.listTopics().namesToListings().get();
			TopicListing topicListing = map.get(name);
			
			Map<String, NewTopic> beans = appContext.getBeansOfType(NewTopic.class);
			NewTopic topic = beans.entrySet().stream().map(e->e.getValue()).filter(nt->nt.name().equals(topicListing.name())).findAny().orElse(null);
			return topic;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
    public Set<String> getTopics() {
        try (Consumer<String, String> consumer = consumerFactory.createConsumer()) {
            Map<String, List<PartitionInfo>> map = consumer.listTopics();
            return map.keySet();
        }
    }
    
    public MessageListenerContainer getListnerByTopicName(String name) {
    	Collection<MessageListenerContainer> listenerContainers = registry.getListenerContainers();
    	MessageListenerContainer listner = null;
    	for (MessageListenerContainer messageListenerContainer : listenerContainers) {
    		String[] topics = messageListenerContainer.getContainerProperties().getTopics();
    		for (int i = 0; i < topics.length; i++) {
				if(topics[i].equals(name)) {
					listner = messageListenerContainer;
				}
			}
		}
    	return listner;
    }
    
    public void startByTopic(String topicName) {
    	MessageListenerContainer listner = getListnerByTopicName(topicName);
    	listner.start();
    }
    
    public void stopByTopic(String topicName) {
    	MessageListenerContainer listner = getListnerByTopicName(topicName);
    	listner.stop();
    }
    
    public void startByListner(String listnerName) {
    	registry.getListenerContainer(listnerName).start();
    }
    
    public void stopByListner(String listnerName) {
    	registry.getListenerContainer(listnerName).stop();
    }

	//TODO
	public void incresePartitionsTo(String topic, int newPartitionNumber) {

	}

}
