package com.github.adminfaces.starter.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.fasten.wp4.kafka.request.client.api.KafkaRequestControllerApi;

@Named
@ApplicationScoped
public class KafkaRequestClient implements Serializable {
	
	
	private KafkaRequestControllerApi kafkaRequestControllerApi;
	
	@PostConstruct
	public void init() {
		kafkaRequestControllerApi = new KafkaRequestControllerApi();
		kafkaRequestControllerApi.getApiClient().setLenientOnJson(true);
		kafkaRequestControllerApi.getApiClient().setDebugging(true);
		kafkaRequestControllerApi.getApiClient().setConnectTimeout(0);
		kafkaRequestControllerApi.getApiClient().setReadTimeout(0);
	}

	@Produces
	public KafkaRequestControllerApi kafkaRequestControllerApi() {
		return kafkaRequestControllerApi;
	}
}
