package com.fasten.wp4.iot.client;

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
	
	@Bean
	public DemandControllerApi demandControllerApi() {
		DemandControllerApi demandControllerApi = new DemandControllerApi();
		demandControllerApi.getApiClient().setLenientOnJson(true);
		demandControllerApi.getApiClient().setDebugging(true);
		demandControllerApi.getApiClient().setConnectTimeout(0);
		demandControllerApi.getApiClient().setReadTimeout(0);
		return demandControllerApi;
	}
	
	@Bean
	public DeliveryControllerApi deliveryControllerApi() {
		DeliveryControllerApi deliveryControllerApi = new DeliveryControllerApi();
		deliveryControllerApi.getApiClient().setLenientOnJson(true);
		deliveryControllerApi.getApiClient().setDebugging(true);
		deliveryControllerApi.getApiClient().setConnectTimeout(0);
		deliveryControllerApi.getApiClient().setReadTimeout(0);
		return deliveryControllerApi;
	}
	
	@Bean
	public PartControllerApi partControllerApi() {
		PartControllerApi partControllerApi = new PartControllerApi();
		partControllerApi.getApiClient().setLenientOnJson(true);
		partControllerApi.getApiClient().setDebugging(true);
		partControllerApi.getApiClient().setConnectTimeout(0);
		partControllerApi.getApiClient().setReadTimeout(0);
		return partControllerApi;
	}
	
	@Bean
	public SramControllerApi sramControllerApi() {
		SramControllerApi sramControllerApi = new SramControllerApi();
		sramControllerApi.getApiClient().setLenientOnJson(true);
		sramControllerApi.getApiClient().setDebugging(true);
		sramControllerApi.getApiClient().setConnectTimeout(0);
		sramControllerApi.getApiClient().setReadTimeout(0);
		return sramControllerApi;
	}
	
	@Bean
	public RemoteStationControllerApi remoteStationControllerApi() {
		RemoteStationControllerApi remoteStationControllerApi = new RemoteStationControllerApi();
		remoteStationControllerApi.getApiClient().setLenientOnJson(true);
		remoteStationControllerApi.getApiClient().setDebugging(true);
		remoteStationControllerApi.getApiClient().setConnectTimeout(0);
		remoteStationControllerApi.getApiClient().setReadTimeout(0);
		return remoteStationControllerApi;
	}
	
	@Bean
	public ProcessingPartControllerApi processingPartControllerApi() {
		ProcessingPartControllerApi processingPartControllerApi = new ProcessingPartControllerApi();
		processingPartControllerApi.getApiClient().setLenientOnJson(true);
		processingPartControllerApi.getApiClient().setDebugging(true);
		processingPartControllerApi.getApiClient().setConnectTimeout(0);
		processingPartControllerApi.getApiClient().setReadTimeout(0);
		return processingPartControllerApi;
	}
	
}
