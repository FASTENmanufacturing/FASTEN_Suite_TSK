package com.fasten.wp4.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.ors.direction.operational.client.api.DirectionsApi;

@Named
@ApplicationScoped
public class OpenRouteServiceDirectionClient implements Serializable {
	
	public static String ORS_API_KEY =  System.getenv("ORS_API_KEY");
	
	private DirectionsApi directionsApi;
	
	@PostConstruct
	public void init() {
		directionsApi = new DirectionsApi();
		directionsApi.getApiClient().setLenientOnJson(true);
		directionsApi.getApiClient().setDebugging(true);
		directionsApi.getApiClient().setConnectTimeout(0);
		directionsApi.getApiClient().setReadTimeout(0);
		directionsApi.getApiClient().setApiKey(ORS_API_KEY);
	}

	@Produces
	public DirectionsApi getDirectionsApi() {
		return directionsApi;
	}

}
