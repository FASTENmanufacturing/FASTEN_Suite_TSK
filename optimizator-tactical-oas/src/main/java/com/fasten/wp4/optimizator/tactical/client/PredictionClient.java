package com.fasten.wp4.optimizator.tactical.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasten.wp4.predictive.client.api.ForecastApi;


@Configuration
public class PredictionClient {
	
	@Value("#{T(java.time.Duration).parse('${ppa.readTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration readTimeout;

	@Value("#{T(java.time.Duration).parse('${ppa.connectTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration connectTimeout;

	@Value("${ppa.debug:false}")
	private Boolean debugging;

	@Value("${ppa.lenient:true}")
	private Boolean lenient;

	@Value("${ppa.url}")
	private String url;
	
	@Bean
	public ForecastApi forecastApi() {
		ForecastApi forecastApi = new ForecastApi();
		forecastApi.getApiClient().setLenientOnJson(lenient);
		forecastApi.getApiClient().setDebugging(debugging);
		forecastApi.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		forecastApi.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		forecastApi.getApiClient().setBasePath(url);
		return forecastApi;
	}
	
}
