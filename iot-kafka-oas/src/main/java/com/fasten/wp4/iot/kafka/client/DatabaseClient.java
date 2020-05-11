package com.fasten.wp4.iot.kafka.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasten.wp4.database.client.api.DeliveryControllerApi;
import com.fasten.wp4.database.client.api.DemandControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.ProcessingPartControllerApi;
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.api.SramControllerApi;

@Configuration
public class DatabaseClient {
	
	
	@Value("#{T(java.time.Duration).parse('${database-oas.readTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration readTimeout;

	@Value("#{T(java.time.Duration).parse('${database-oas.connectTimeout:PT3S}')}") //defauls to 3 seconds
	private Duration connectTimeout;

	@Value("${database-oas.debug:false}")
	private Boolean debugging;

	@Value("${database-oas.lenient:true}")
	private Boolean lenient;

	@Value("${database-oas.url}")
	private String url;
	
	@Bean
	public DemandControllerApi demandControllerApi() {
		DemandControllerApi demandControllerApi = new DemandControllerApi();
		demandControllerApi.getApiClient().setLenientOnJson(lenient);
		demandControllerApi.getApiClient().setDebugging(debugging);
		demandControllerApi.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		demandControllerApi.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		demandControllerApi.getApiClient().setBasePath(url);
		return demandControllerApi;
	}
	
	@Bean
	public DeliveryControllerApi deliveryControllerApi() {
		DeliveryControllerApi deliveryControllerApi = new DeliveryControllerApi();
		deliveryControllerApi.getApiClient().setLenientOnJson(lenient);
		deliveryControllerApi.getApiClient().setDebugging(debugging);
		deliveryControllerApi.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		deliveryControllerApi.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		deliveryControllerApi.getApiClient().setBasePath(url);
		return deliveryControllerApi;
	}
	
	@Bean
	public PartControllerApi partControllerApi() {
		PartControllerApi partControllerApi = new PartControllerApi();
		partControllerApi.getApiClient().setLenientOnJson(lenient);
		partControllerApi.getApiClient().setDebugging(debugging);
		partControllerApi.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		partControllerApi.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		partControllerApi.getApiClient().setBasePath(url);
		return partControllerApi;
	}
	
	@Bean
	public SramControllerApi sramControllerApi() {
		SramControllerApi sramControllerApi = new SramControllerApi();
		sramControllerApi.getApiClient().setLenientOnJson(lenient);
		sramControllerApi.getApiClient().setDebugging(debugging);
		sramControllerApi.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		sramControllerApi.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		sramControllerApi.getApiClient().setBasePath(url);
		return sramControllerApi;
	}
	
	@Bean
	public RemoteStationControllerApi remoteStationControllerApi() {
		RemoteStationControllerApi remoteStationControllerApi = new RemoteStationControllerApi();
		remoteStationControllerApi.getApiClient().setLenientOnJson(lenient);
		remoteStationControllerApi.getApiClient().setDebugging(debugging);
		remoteStationControllerApi.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		remoteStationControllerApi.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		remoteStationControllerApi.getApiClient().setBasePath(url);
		return remoteStationControllerApi;
	}
	
	@Bean
	public ProcessingPartControllerApi processingPartControllerApi() {
		ProcessingPartControllerApi processingPartControllerApi = new ProcessingPartControllerApi();
		processingPartControllerApi.getApiClient().setLenientOnJson(lenient);
		processingPartControllerApi.getApiClient().setDebugging(debugging);
		processingPartControllerApi.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		processingPartControllerApi.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		processingPartControllerApi.getApiClient().setBasePath(url);
		return processingPartControllerApi;
	}
	
}
