package com.fasten.wp4.optimizator.tactical.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasten.wp4.email.client.api.EmailControllerApi;


@Configuration
public class EmailClient {
	
	@Bean
	public EmailControllerApi emailControllerApi() {
		EmailControllerApi emailControllerApi = new EmailControllerApi();
		emailControllerApi.getApiClient().setLenientOnJson(true);
		emailControllerApi.getApiClient().setDebugging(false);
		emailControllerApi.getApiClient().setConnectTimeout(0);
		emailControllerApi.getApiClient().setReadTimeout(0);
		return emailControllerApi;
	}
	
}
