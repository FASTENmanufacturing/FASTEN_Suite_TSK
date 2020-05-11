package com.fasten.wp4.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.fasten.wp4.predictive.client.api.ForecastApi;

@Named
@ApplicationScoped
public class PredictionClient implements Serializable {
	
	public static String PPA_URL =  System.getenv("PPA_URL");
	
	private ForecastApi forecastApi;
	
	@PostConstruct
	public void init() {
		forecastApi = new ForecastApi();
		forecastApi.getApiClient().setLenientOnJson(true);
		forecastApi.getApiClient().setDebugging(true);
		forecastApi.getApiClient().setConnectTimeout(0);
		forecastApi.getApiClient().setReadTimeout(0);
		forecastApi.getApiClient().setBasePath(PPA_URL);
	}

	@Produces
	public ForecastApi getForecastApi() {
		return forecastApi;
	}

}
