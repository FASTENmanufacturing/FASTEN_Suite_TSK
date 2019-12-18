package com.github.adminfaces.starter.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.ors.matrix.client.api.MatrixApi;

@Named
@ApplicationScoped
public class OpenRouteServiceMatrixClient implements Serializable {
	
	private MatrixApi matrixApi;

	@PostConstruct
	public void init() {
		matrixApi = new MatrixApi();
		matrixApi.getApiClient().setLenientOnJson(true);
		matrixApi.getApiClient().setDebugging(true);
		matrixApi.getApiClient().setConnectTimeout(0);
		matrixApi.getApiClient().setReadTimeout(0);
	}

	@Produces
	public MatrixApi getMatrixApi() {
		return matrixApi;
	}

}
