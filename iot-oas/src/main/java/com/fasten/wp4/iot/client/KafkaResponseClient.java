package com.fasten.wp4.iot.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasten.wp4.kafka.response.client.api.KafkaResponseControllerApi;

@Configuration
public class KafkaResponseClient {
	
	@Bean
	public KafkaResponseControllerApi kafkaResponseControllerApi() {
		KafkaResponseControllerApi kafkaResponseControllerApi = new KafkaResponseControllerApi();
		kafkaResponseControllerApi.getApiClient().setLenientOnJson(true);
		kafkaResponseControllerApi.getApiClient().setDebugging(true);
		return kafkaResponseControllerApi;
	}
	
}
