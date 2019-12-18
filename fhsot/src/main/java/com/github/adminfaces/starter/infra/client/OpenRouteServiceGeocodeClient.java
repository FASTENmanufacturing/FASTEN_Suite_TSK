package com.github.adminfaces.starter.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.ors.geocode.client.api.GeocodeApi;

@Named
@ApplicationScoped
public class OpenRouteServiceGeocodeClient implements Serializable {
	
	
	private GeocodeApi geocodeApi;
	
	@PostConstruct
	public void init() {
		geocodeApi = new GeocodeApi();
		geocodeApi.getApiClient().setLenientOnJson(true);
		geocodeApi.getApiClient().setDebugging(true);
		geocodeApi.getApiClient().setConnectTimeout(0);
		geocodeApi.getApiClient().setReadTimeout(0);
	}

	@Produces
	public GeocodeApi getGeocodeApi() {
		return geocodeApi;
	}

}
