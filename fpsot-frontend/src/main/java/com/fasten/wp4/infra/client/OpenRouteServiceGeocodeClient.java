package com.fasten.wp4.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.ors.geocode.client.api.GeocodeApi;

@Named
@ApplicationScoped
public class OpenRouteServiceGeocodeClient implements Serializable {
	
	public static String ORS_API_KEY =  System.getenv("ORS_API_KEY");
	
	private GeocodeApi geocodeApi;
	
	@PostConstruct
	public void init() {
		geocodeApi = new GeocodeApi();
		geocodeApi.getApiClient().setLenientOnJson(true);
		geocodeApi.getApiClient().setDebugging(true);
		geocodeApi.getApiClient().setConnectTimeout(0);
		geocodeApi.getApiClient().setReadTimeout(0);
		geocodeApi.getApiClient().setApiKey(ORS_API_KEY);
	}

	@Produces
	public GeocodeApi getGeocodeApi() {
		return geocodeApi;
	}

}
