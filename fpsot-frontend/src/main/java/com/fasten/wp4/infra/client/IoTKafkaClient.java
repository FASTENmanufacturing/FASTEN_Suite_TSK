package com.fasten.wp4.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.fasten.wp4.iot.kafka.client.api.KafkaControllerApi;

@Named
@ApplicationScoped
public class IoTKafkaClient implements Serializable {
	
	public static String IOT_KAFKA_OAS_URL =  System.getenv("IOT_KAFKA_OAS_URL");
	
	private KafkaControllerApi kafkaControllerApi;
	
	@PostConstruct
	public void init() {
		kafkaControllerApi = new KafkaControllerApi();
		kafkaControllerApi.getApiClient().setLenientOnJson(true);
		kafkaControllerApi.getApiClient().setDebugging(true);
		kafkaControllerApi.getApiClient().setConnectTimeout(0);
		kafkaControllerApi.getApiClient().setReadTimeout(0);
		kafkaControllerApi.getApiClient().setBasePath(IOT_KAFKA_OAS_URL);
	}

	@Produces
	public KafkaControllerApi kafkaControllerApi() {
		return kafkaControllerApi;
	}
}
