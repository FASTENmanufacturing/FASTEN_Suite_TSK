package com.github.adminfaces.starter.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;


import com.fasten.wp4.email.client.api.EmailControllerApi;

@Named
@ApplicationScoped
public class EmailClient implements Serializable {
	
	private boolean debug=true;
	
	private EmailControllerApi emailControllerApi;
	
	@PostConstruct
	public void init() {
		
		//Input
		emailControllerApi = new EmailControllerApi();
		emailControllerApi.getApiClient().setLenientOnJson(true);
		emailControllerApi.getApiClient().setDebugging(debug);
		emailControllerApi.getApiClient().setConnectTimeout(0);
		emailControllerApi.getApiClient().setReadTimeout(0);
	}

	@Produces
	public EmailControllerApi getEmailControllerApi() {
		return emailControllerApi;
	}
}
