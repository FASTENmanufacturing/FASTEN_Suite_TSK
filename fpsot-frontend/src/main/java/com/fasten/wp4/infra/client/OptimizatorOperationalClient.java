package com.fasten.wp4.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.fasten.wp4.optimizator.operational.client.api.DefaultApi;

@Named
@ApplicationScoped
public class OptimizatorOperationalClient implements Serializable {
	
	public static String OPTIMIZATOR_OPERATIONAL_URL =  System.getenv("OPTIMIZATOR_OPERATIONAL_URL");
	
	private DefaultApi defaultApi;
	

	@PostConstruct
	public void init() {
		defaultApi = new DefaultApi();
		defaultApi.getApiClient().setLenientOnJson(true);
		defaultApi.getApiClient().setDebugging(true);
		defaultApi.getApiClient().setConnectTimeout(0);
		defaultApi.getApiClient().setReadTimeout(0);
		defaultApi.getApiClient().setBasePath(OPTIMIZATOR_OPERATIONAL_URL);
	}

	@Produces
	public DefaultApi getDefaultApi() {
		return defaultApi;
	}
	
}
