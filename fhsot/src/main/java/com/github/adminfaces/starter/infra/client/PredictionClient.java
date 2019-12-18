package com.github.adminfaces.starter.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.fasten.wp4.predictive.client.api.ForecastingMethodsApi;

@Named
@ApplicationScoped
public class PredictionClient implements Serializable {
	
	
	private ForecastingMethodsApi forecastingMethodsApi;
	
	@PostConstruct
	public void init() {
		forecastingMethodsApi = new ForecastingMethodsApi();
		forecastingMethodsApi.getApiClient().setLenientOnJson(true);
		forecastingMethodsApi.getApiClient().setDebugging(true);
		forecastingMethodsApi.getApiClient().setConnectTimeout(0);
		forecastingMethodsApi.getApiClient().setReadTimeout(0);
	}

	@Produces
	public ForecastingMethodsApi getForecastingMethodsApi() {
		return forecastingMethodsApi;
	}

}
