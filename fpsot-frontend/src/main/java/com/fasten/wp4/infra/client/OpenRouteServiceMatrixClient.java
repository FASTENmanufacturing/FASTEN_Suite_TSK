package com.fasten.wp4.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.ors.matrix.client.api.MatrixApi;

@Named
@ApplicationScoped
public class OpenRouteServiceMatrixClient implements Serializable {

	public static String ORS_API_KEY =  System.getenv("ORS_API_KEY");
	
	private MatrixApi matrixApi;

	@PostConstruct
	public void init() {
		matrixApi = new MatrixApi();
		matrixApi.getApiClient().setLenientOnJson(true);
		matrixApi.getApiClient().setDebugging(true);
		matrixApi.getApiClient().setConnectTimeout(0);
		matrixApi.getApiClient().setReadTimeout(0);
		matrixApi.getApiClient().setApiKey(ORS_API_KEY);
	}

	@Produces
	public MatrixApi getMatrixApi() {
		return matrixApi;
	}

}
