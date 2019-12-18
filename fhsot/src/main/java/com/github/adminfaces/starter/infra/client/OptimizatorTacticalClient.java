package com.github.adminfaces.starter.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.fasten.wp4.optimizator.tactical.client.api.TacticalOptimizationControllerApi;

@Named
@ApplicationScoped
public class OptimizatorTacticalClient implements Serializable {
	
	
	private TacticalOptimizationControllerApi tacticalOptimizationControllerApi;
	

	@PostConstruct
	public void init() {
		tacticalOptimizationControllerApi = new TacticalOptimizationControllerApi();
		tacticalOptimizationControllerApi.getApiClient().setLenientOnJson(true);
		tacticalOptimizationControllerApi.getApiClient().setDebugging(true);
		tacticalOptimizationControllerApi.getApiClient().setConnectTimeout(0);
		tacticalOptimizationControllerApi.getApiClient().setReadTimeout(0);
	}

	@Produces
	public TacticalOptimizationControllerApi getDefaultApi() {
		return tacticalOptimizationControllerApi;
	}
	
}
