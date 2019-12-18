package com.github.adminfaces.starter.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.fasten.wp4.optimizator.operational.client.api.DefaultApi;

@Named
@ApplicationScoped
public class OptimizatorOperationalClient implements Serializable {
	
	
	private DefaultApi defaultApi;
	

	@PostConstruct
	public void init() {
		defaultApi = new DefaultApi();
		defaultApi.getApiClient().setLenientOnJson(true);
		defaultApi.getApiClient().setDebugging(true);
		defaultApi.getApiClient().setConnectTimeout(0);
		defaultApi.getApiClient().setReadTimeout(0);
	}

	@Produces
	public DefaultApi getDefaultApi() {
		return defaultApi;
	}
	
}
