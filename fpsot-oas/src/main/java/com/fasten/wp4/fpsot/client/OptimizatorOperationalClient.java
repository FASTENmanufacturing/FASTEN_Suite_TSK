package com.fasten.wp4.fpsot.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasten.wp4.optimizator.operational.client.api.DefaultApi;


@Configuration
public class OptimizatorOperationalClient {
	
	@Value("#{T(java.time.Duration).parse('${optimizator-operational-oas.readTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration readTimeout;

	@Value("#{T(java.time.Duration).parse('${optimizator-operational-oas.connectTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration connectTimeout;

	@Value("${optimizator-operational-oas.debug:false}")
	private Boolean debugging;

	@Value("${optimizator-operational-oas.lenient:true}")
	private Boolean lenient;

	@Value("${optimizator-operational-oas.url}")
	private String url;
	
	@Bean
	public DefaultApi optimizatorOperationalApi() {
		DefaultApi api = new DefaultApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}
	
}
