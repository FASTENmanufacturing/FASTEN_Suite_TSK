package com.fasten.wp4.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.fasten.wp4.probabilitydistribution.client.api.DefaultApi;

@Named
@ApplicationScoped
public class ProbabilityDistributionClient implements Serializable {
	
	public static String PROBABILITY_DISTRIBUTION_URL =  System.getenv("PROBABILITY_DISTRIBUTION_URL");
	
	private DefaultApi defaultApi;
	

	@PostConstruct
	public void init() {
		defaultApi = new DefaultApi();
		defaultApi.getApiClient().setLenientOnJson(true);
		defaultApi.getApiClient().setDebugging(true);
		defaultApi.getApiClient().setConnectTimeout(0);
		defaultApi.getApiClient().setReadTimeout(0);
		defaultApi.getApiClient().setBasePath(PROBABILITY_DISTRIBUTION_URL);
	}

	@Produces
	public DefaultApi getDefaultApi() {
		return defaultApi;
	}
	
}
