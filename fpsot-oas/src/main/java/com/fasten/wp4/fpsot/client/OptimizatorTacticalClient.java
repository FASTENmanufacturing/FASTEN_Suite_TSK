package com.fasten.wp4.fpsot.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasten.wp4.optimizator.tactical.client.api.TacticalOptimizationControllerApi;
import com.fasten.wp4.predictive.client.api.ForecastApi;


@Configuration
public class OptimizatorTacticalClient {
	
	@Value("#{T(java.time.Duration).parse('${optimizator-tactical-oas.readTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration readTimeout;

	@Value("#{T(java.time.Duration).parse('${optimizator-tactical-oas.connectTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration connectTimeout;

	@Value("${optimizator-tactical-oas.debug:false}")
	private Boolean debugging;

	@Value("${optimizator-tactical-oas.lenient:true}")
	private Boolean lenient;

	@Value("${optimizator-tactical-oas.url}")
	private String url;
	
	@Bean
	public TacticalOptimizationControllerApi tacticalOptimizationApi() {
		TacticalOptimizationControllerApi api = new TacticalOptimizationControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}
	
}
