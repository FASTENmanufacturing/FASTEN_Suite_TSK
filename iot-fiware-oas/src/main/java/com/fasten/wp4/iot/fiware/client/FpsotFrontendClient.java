package com.fasten.wp4.iot.fiware.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasten.wp4.fpsot.frontend.client.api.FrontendNotifyControllerApi;

@Configuration
public class FpsotFrontendClient {
	
	@Value("#{T(java.time.Duration).parse('${fpsot-frontend-client.readTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration readTimeout;

	@Value("#{T(java.time.Duration).parse('${fpsot-frontend-client.connectTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration connectTimeout;

	@Value("${fpsot-frontend-client.debug:false}")
	private Boolean debugging;

	@Value("${fpsot-frontend-client.lenient:true}")
	private Boolean lenient;

	@Value("${fpsot-frontend-client.url}")
	private String url;
	
	@Bean
	public FrontendNotifyControllerApi frontendNotifyControllerApi() {
		FrontendNotifyControllerApi frontendNotifyControllerApi = new FrontendNotifyControllerApi();
		
		frontendNotifyControllerApi.getApiClient().setLenientOnJson(lenient);
		frontendNotifyControllerApi.getApiClient().setDebugging(debugging);
		frontendNotifyControllerApi.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		frontendNotifyControllerApi.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		frontendNotifyControllerApi.getApiClient().setBasePath(url);
		
		return frontendNotifyControllerApi;
	}
	
	
}
