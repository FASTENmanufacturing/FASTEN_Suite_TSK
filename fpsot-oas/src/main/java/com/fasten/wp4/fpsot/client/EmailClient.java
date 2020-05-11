package com.fasten.wp4.fpsot.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasten.wp4.email.client.api.EmailControllerApi;


@Configuration
public class EmailClient {
	
	@Value("#{T(java.time.Duration).parse('${email-oas.readTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration readTimeout;

	@Value("#{T(java.time.Duration).parse('${email-oas.connectTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration connectTimeout;

	@Value("${email-oas.debug:false}")
	private Boolean debugging;

	@Value("${email-oas.lenient:true}")
	private Boolean lenient;

	@Value("${email-oas.url}")
	private String url;
	
	@Bean
	public EmailControllerApi emailControllerApi() {
		EmailControllerApi emailControllerApi = new EmailControllerApi();
		emailControllerApi.getApiClient().setLenientOnJson(lenient);
		emailControllerApi.getApiClient().setDebugging(debugging);
		emailControllerApi.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		emailControllerApi.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		emailControllerApi.getApiClient().setBasePath(url);
		return emailControllerApi;
	}
	
}
