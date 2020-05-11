package com.fasten.wp4.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.fasten.wp4.fpsot.client.api.HsoControllerApi;

@Named
@ApplicationScoped
public class FpsotClient implements Serializable {
	
	public static String FPSOT_OAS_URL =  System.getenv("FPSOT_OAS_URL");
	
	private boolean debug=true;
	
	private HsoControllerApi hsoControllerApi;
	
//	private PpaControllerApi ppaControllerApi;
	
	@PostConstruct
	public void init() {
		
		hsoControllerApi = new HsoControllerApi();
		hsoControllerApi.getApiClient().setLenientOnJson(true);
		hsoControllerApi.getApiClient().setDebugging(debug);
		hsoControllerApi.getApiClient().setConnectTimeout(0);
		hsoControllerApi.getApiClient().setReadTimeout(0);
		hsoControllerApi.getApiClient().setBasePath(FPSOT_OAS_URL);

//		ppaControllerApi = new HsoControllerApi();
//		ppaControllerApi.getApiClient().setLenientOnJson(true);
//		ppaControllerApi.getApiClient().setDebugging(debug);
//		ppaControllerApi.getApiClient().setConnectTimeout(0);
//		ppaControllerApi.getApiClient().setReadTimeout(0);
//		ppaControllerApi.getApiClient().setBasePath(FPSOT_OAS_URL);
		
	}

	@Produces
	public HsoControllerApi getHsoControllerApi() {
		return hsoControllerApi;
	}

//	@Produces
//	public PpaControllerApi getPpaControllerApi() {
//		return ppaControllerApi;
//	}
	
}
