package com.fasten.wp4.iot.fiware.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasten.wp4.orion.client.api.ApiEntryPointApi;
import com.fasten.wp4.orion.client.api.AttributeValueApi;
import com.fasten.wp4.orion.client.api.AttributesApi;
import com.fasten.wp4.orion.client.api.BatchOperationsApi;
import com.fasten.wp4.orion.client.api.EntitiesApi;
import com.fasten.wp4.orion.client.api.RegistrationsApi;
import com.fasten.wp4.orion.client.api.SubscriptionsApi;
import com.fasten.wp4.orion.client.api.TypesApi;

@Configuration
public class OrionClient {
	
	
	@Value("#{T(java.time.Duration).parse('${orion-client.readTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration readTimeout;

	@Value("#{T(java.time.Duration).parse('${orion-client.connectTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration connectTimeout;

	@Value("${orion-client.debug:false}")
	private Boolean debugging;

	@Value("${orion-client.lenient:true}")
	private Boolean lenient;

	@Value("${orion-client.url}")
	private String url;
	
	@Bean
	public ApiEntryPointApi apiEntryPointApi() {
		ApiEntryPointApi api = new ApiEntryPointApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}
	
	@Bean
	public AttributesApi attributesApi() {
		AttributesApi api = new AttributesApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}
	
	@Bean
	public AttributeValueApi attributeValueApi() {
		AttributeValueApi api = new AttributeValueApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}
	
	@Bean
	public BatchOperationsApi batchOperationsApi() {
		BatchOperationsApi batchOperationsApi = new BatchOperationsApi();
		batchOperationsApi.getApiClient().setLenientOnJson(lenient);
		batchOperationsApi.getApiClient().setDebugging(debugging);
		batchOperationsApi.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		batchOperationsApi.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		batchOperationsApi.getApiClient().setBasePath(url);
		return batchOperationsApi;
	}
	
	@Bean
	public EntitiesApi entitiesApi() {
		EntitiesApi api = new EntitiesApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}
	
	@Bean
	public RegistrationsApi registrationsApi() {
		RegistrationsApi api = new RegistrationsApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public SubscriptionsApi subscriptionsApi() {
		SubscriptionsApi api = new SubscriptionsApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public TypesApi typesApi() {
		TypesApi api = new TypesApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}
	
}
