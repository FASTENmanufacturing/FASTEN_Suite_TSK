package com.fasten.wp4.fpsot.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasten.wp4.database.client.api.AllocatedSramControllerApi;
import com.fasten.wp4.database.client.api.CityControllerApi;
import com.fasten.wp4.database.client.api.DeliveryControllerApi;
import com.fasten.wp4.database.client.api.DemandControllerApi;
import com.fasten.wp4.database.client.api.DemandProjectedControllerApi;
import com.fasten.wp4.database.client.api.DemandProjectionStudyControllerApi;
import com.fasten.wp4.database.client.api.InternalSupplyControllerApi;
import com.fasten.wp4.database.client.api.OperationalOptimizationControllerApi;
import com.fasten.wp4.database.client.api.OperationalOptimizationResultControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.PredictionControllerApi;
import com.fasten.wp4.database.client.api.ProcessingPartControllerApi;
import com.fasten.wp4.database.client.api.ProductionControllerApi;
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.api.RouteControllerApi;
import com.fasten.wp4.database.client.api.SramControllerApi;
import com.fasten.wp4.database.client.api.StateControllerApi;
import com.fasten.wp4.database.client.api.TacticalOptimizationControllerApi;
import com.fasten.wp4.database.client.api.TacticalOptimizationResultControllerApi;
import com.fasten.wp4.database.client.api.WebHookControllerApi;

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
	public AllocatedSramControllerApi allocatedSramControllerApi() {
		AllocatedSramControllerApi api = new AllocatedSramControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public CityControllerApi cityControllerApi() {
		CityControllerApi api = new CityControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public DeliveryControllerApi deliveryControllerApi() {
		DeliveryControllerApi api = new DeliveryControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public DemandControllerApi demandControllerApi() {
		DemandControllerApi api = new DemandControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}
	
	@Bean
	public DemandProjectedControllerApi demandProjectedControllerApi() {
		DemandProjectedControllerApi api = new DemandProjectedControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public DemandProjectionStudyControllerApi demandProjectionStudyControllerApi() {
		DemandProjectionStudyControllerApi api = new DemandProjectionStudyControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public InternalSupplyControllerApi internalSupplyControllerApi() {
		InternalSupplyControllerApi api = new InternalSupplyControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public OperationalOptimizationControllerApi operationalOptimizationControllerApi() {
		OperationalOptimizationControllerApi api = new OperationalOptimizationControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public OperationalOptimizationResultControllerApi operationalOptimizationResultControllerApi() {
		OperationalOptimizationResultControllerApi api = new OperationalOptimizationResultControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public PartControllerApi partControllerApi() {
		PartControllerApi api = new PartControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}
	
	@Bean
	public PredictionControllerApi predictionControllerApi() {
		PredictionControllerApi api = new PredictionControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public ProcessingPartControllerApi processingPartControllerApi() {
		ProcessingPartControllerApi api = new ProcessingPartControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public ProductionControllerApi productionControllerApi() {
		ProductionControllerApi api = new ProductionControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public RemoteStationControllerApi remoteStationControllerApi() {
		RemoteStationControllerApi api = new RemoteStationControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public RouteControllerApi routeControllerApi() {
		RouteControllerApi api = new RouteControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public SramControllerApi sramControllerApi() {
		SramControllerApi api = new SramControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}
	
	@Bean
	public StateControllerApi stateControllerApi() {
		StateControllerApi api = new StateControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public TacticalOptimizationControllerApi tacticalOptimizationControllerApi() {
		TacticalOptimizationControllerApi api = new TacticalOptimizationControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}
	
	@Bean
	public TacticalOptimizationResultControllerApi tacticalOptimizationResultControllerApi() {
		TacticalOptimizationResultControllerApi api = new TacticalOptimizationResultControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}

	@Bean
	public WebHookControllerApi webHookControllerApi() {
		WebHookControllerApi api = new WebHookControllerApi();
		api.getApiClient().setLenientOnJson(lenient);
		api.getApiClient().setDebugging(debugging);
		api.getApiClient().setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
		api.getApiClient().setReadTimeout(Math.toIntExact(readTimeout.toMillis()));
		api.getApiClient().setBasePath(url);
		return api;
	}
			
	
}
