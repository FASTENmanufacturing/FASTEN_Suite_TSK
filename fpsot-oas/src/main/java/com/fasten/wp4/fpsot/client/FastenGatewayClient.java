package com.fasten.wp4.fpsot.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasten.wp5.fastengateway.client.api.DefaultApi;


@Configuration
public class FastenGatewayClient {
	
	@Value("#{T(java.time.Duration).parse('${fastengateway-client.readTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration readTimeout;

	@Value("#{T(java.time.Duration).parse('${fastengateway-client.connectTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration connectTimeout;

	@Value("${fastengateway-client.debug:false}")
	private Boolean debugging;

	@Value("${fastengateway-client.lenient:true}")
	private Boolean lenient;

	@Value("${fastengateway-client.url}")
	private String url;
	
	@Bean
	public DefaultApi fastenGatewayApi() {
		DefaultApi fastenGatewayApi = new DefaultApi();
		fastenGatewayApi.getApiClient().setLenientOnJson(lenient);
		fastenGatewayApi.getApiClient().setDebugging(debugging);
		fastenGatewayApi.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		fastenGatewayApi.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		fastenGatewayApi.getApiClient().setBasePath(url);
		return fastenGatewayApi;
	}
	
}
