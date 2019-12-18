package com.github.adminfaces.starter.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.ors.direction.operational.client.api.DirectionsApi;

@Named
@ApplicationScoped
public class OpenRouteServiceDirectionClient implements Serializable {
	
	
	private DirectionsApi directionsApi;
	
	@PostConstruct
	public void init() {
		directionsApi = new DirectionsApi();
		directionsApi.getApiClient().setLenientOnJson(true);
		directionsApi.getApiClient().setDebugging(true);
		directionsApi.getApiClient().setConnectTimeout(0);
		directionsApi.getApiClient().setReadTimeout(0);
	}

	@Produces
	public DirectionsApi getDirectionsApi() {
		return directionsApi;
	}

}
