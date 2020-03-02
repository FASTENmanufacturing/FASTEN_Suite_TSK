package com.fasten.wp4.optimizator.tactical.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasten.wp4.predictive.client.api.ForecastApi;


@Configuration
public class PredictionClient {
	
	
	@Bean
	public ForecastApi forecastApi() {
		ForecastApi forecastApi = new ForecastApi();
		forecastApi.getApiClient().setLenientOnJson(true);
		forecastApi.getApiClient().setDebugging(true);
		forecastApi.getApiClient().setConnectTimeout(0);
		forecastApi.getApiClient().setReadTimeout(0);
		return forecastApi;
	}
	
}
