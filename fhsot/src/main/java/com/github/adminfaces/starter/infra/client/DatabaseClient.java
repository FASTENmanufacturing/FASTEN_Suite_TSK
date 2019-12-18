package com.github.adminfaces.starter.infra.client;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.fasten.wp4.database.client.api.AllocatedSramControllerApi;
import com.fasten.wp4.database.client.api.CityControllerApi;
import com.fasten.wp4.database.client.api.DeliveryControllerApi;
import com.fasten.wp4.database.client.api.DemandControllerApi;
import com.fasten.wp4.database.client.api.DemandProjectedControllerApi;
import com.fasten.wp4.database.client.api.DemandProjectionStudyControllerApi;
import com.fasten.wp4.database.client.api.InternalOrderControllerApi;
import com.fasten.wp4.database.client.api.InternalSupplyControllerApi;
import com.fasten.wp4.database.client.api.OperationalOptimizationControllerApi;
import com.fasten.wp4.database.client.api.OperationalOptimizationResultControllerApi;
import com.fasten.wp4.database.client.api.OptimizatorControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.PredictionControllerApi;
import com.fasten.wp4.database.client.api.ProcessingPartControllerApi;
import com.fasten.wp4.database.client.api.ProductionControllerApi;
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.api.RouteControllerApi;
import com.fasten.wp4.database.client.api.SramControllerApi;
import com.fasten.wp4.database.client.api.StateControllerApi;
import com.fasten.wp4.database.client.api.StockControllerApi;
import com.fasten.wp4.database.client.api.TacticalOptimizationControllerApi;
import com.fasten.wp4.database.client.api.TacticalOptimizationResultControllerApi;

@Named
@ApplicationScoped
public class DatabaseClient implements Serializable {
	
	private boolean debug=true;
	
	//Input
	private DeliveryControllerApi deliveryControllerApi;
	private DemandControllerApi demandControllerApi;
	private PartControllerApi partControllerApi;
	private AllocatedSramControllerApi allocatedSramControllerApi;
	private ProcessingPartControllerApi processingPartControllerApi;
	private RemoteStationControllerApi remoteStationControllerApi;
	
	//Input form
	private TacticalOptimizationControllerApi tacticalOptimizationControllerApi;
	private PredictionControllerApi predictiveControllerApi;
	private OperationalOptimizationControllerApi operationalOptimizationControllerApi;
	private CityControllerApi cityControllerApi;
	private StateControllerApi stateControllerApi;
	
	//Grafana
	private DemandProjectedControllerApi demandProjectedControllerApi;
	private DemandProjectionStudyControllerApi demandProjectionStudyControllerApi;
	
	//Output
	private InternalOrderControllerApi internalOrderControllerApi;
	private InternalSupplyControllerApi internalSupplyControllerApi;
	private OptimizatorControllerApi optimizatorControllerApi;
	private SramControllerApi sramControllerApi;
	private RouteControllerApi routeControllerApi;
	private StockControllerApi stockControllerApi;
	private ProductionControllerApi productionControllerApi;
	
	private OperationalOptimizationResultControllerApi operationalOptimizationResultControllerApi;
	private TacticalOptimizationResultControllerApi tacticalOptimizationResultControllerApi;
	
	@PostConstruct
	public void init() {
		
		//Input
		deliveryControllerApi = new DeliveryControllerApi();
		deliveryControllerApi.getApiClient().setLenientOnJson(true);
		deliveryControllerApi.getApiClient().setDebugging(debug);
		deliveryControllerApi.getApiClient().setConnectTimeout(0);
		deliveryControllerApi.getApiClient().setReadTimeout(0);
		
		demandControllerApi = new DemandControllerApi();
		demandControllerApi.getApiClient().setLenientOnJson(true);
		demandControllerApi.getApiClient().setDebugging(debug);
		demandControllerApi.getApiClient().setConnectTimeout(0);
		demandControllerApi.getApiClient().setReadTimeout(0);
		
		partControllerApi = new PartControllerApi();
		partControllerApi.getApiClient().setLenientOnJson(true);
		partControllerApi.getApiClient().setDebugging(debug);
		partControllerApi.getApiClient().setConnectTimeout(0);
		partControllerApi.getApiClient().setReadTimeout(0);
		
		sramControllerApi = new SramControllerApi();
		sramControllerApi.getApiClient().setLenientOnJson(true);
		sramControllerApi.getApiClient().setDebugging(debug);
		sramControllerApi.getApiClient().setConnectTimeout(0);
		sramControllerApi.getApiClient().setReadTimeout(0);
		
		processingPartControllerApi = new ProcessingPartControllerApi();
		processingPartControllerApi.getApiClient().setLenientOnJson(true);
		processingPartControllerApi.getApiClient().setDebugging(debug);
		processingPartControllerApi.getApiClient().setConnectTimeout(0);
		processingPartControllerApi.getApiClient().setReadTimeout(0);
		
		remoteStationControllerApi = new RemoteStationControllerApi();
		remoteStationControllerApi.getApiClient().setLenientOnJson(true);
		remoteStationControllerApi.getApiClient().setDebugging(debug);
		remoteStationControllerApi.getApiClient().setConnectTimeout(0);
		remoteStationControllerApi.getApiClient().setReadTimeout(0);
		
		//Grafana
		demandProjectedControllerApi = new DemandProjectedControllerApi();
		demandProjectedControllerApi.getApiClient().setLenientOnJson(true);
		demandProjectedControllerApi.getApiClient().setDebugging(debug);
		demandProjectedControllerApi.getApiClient().setConnectTimeout(0);
		demandProjectedControllerApi.getApiClient().setReadTimeout(0);
		
		demandProjectionStudyControllerApi = new DemandProjectionStudyControllerApi();
		demandProjectionStudyControllerApi.getApiClient().setLenientOnJson(true);
		demandProjectionStudyControllerApi.getApiClient().setDebugging(debug);
		demandProjectionStudyControllerApi.getApiClient().setConnectTimeout(0);
		demandProjectionStudyControllerApi.getApiClient().setReadTimeout(0);
		
		//Output
		internalOrderControllerApi = new InternalOrderControllerApi();
		internalSupplyControllerApi = new InternalSupplyControllerApi();
		optimizatorControllerApi = new OptimizatorControllerApi();
		allocatedSramControllerApi = new AllocatedSramControllerApi();
		routeControllerApi = new RouteControllerApi();
		stockControllerApi = new StockControllerApi();
		productionControllerApi = new ProductionControllerApi();
		
		operationalOptimizationResultControllerApi = new OperationalOptimizationResultControllerApi();
		operationalOptimizationResultControllerApi.getApiClient().setLenientOnJson(true);
		operationalOptimizationResultControllerApi.getApiClient().setDebugging(debug);
		operationalOptimizationResultControllerApi.getApiClient().setConnectTimeout(0);
		operationalOptimizationResultControllerApi.getApiClient().setReadTimeout(0);

		tacticalOptimizationResultControllerApi = new TacticalOptimizationResultControllerApi();
		tacticalOptimizationResultControllerApi.getApiClient().setLenientOnJson(true);
		tacticalOptimizationResultControllerApi.getApiClient().setDebugging(debug);
		tacticalOptimizationResultControllerApi.getApiClient().setConnectTimeout(0);
		tacticalOptimizationResultControllerApi.getApiClient().setReadTimeout(0);
		
		//Form
		tacticalOptimizationControllerApi = new TacticalOptimizationControllerApi();
		tacticalOptimizationControllerApi.getApiClient().setLenientOnJson(true);
		tacticalOptimizationControllerApi.getApiClient().setDebugging(debug);
		tacticalOptimizationControllerApi.getApiClient().setConnectTimeout(0);
		tacticalOptimizationControllerApi.getApiClient().setReadTimeout(0);
		
		predictiveControllerApi = new PredictionControllerApi();
		predictiveControllerApi.getApiClient().setLenientOnJson(true);
		predictiveControllerApi.getApiClient().setDebugging(debug);
		predictiveControllerApi.getApiClient().setConnectTimeout(0);
		predictiveControllerApi.getApiClient().setReadTimeout(0);
		
		operationalOptimizationControllerApi = new OperationalOptimizationControllerApi();
		operationalOptimizationControllerApi.getApiClient().setLenientOnJson(true);
		operationalOptimizationControllerApi.getApiClient().setDebugging(debug);
		operationalOptimizationControllerApi.getApiClient().setConnectTimeout(0);
		operationalOptimizationControllerApi.getApiClient().setReadTimeout(0);
		
		cityControllerApi = new CityControllerApi();
		cityControllerApi.getApiClient().setLenientOnJson(true);
		cityControllerApi.getApiClient().setDebugging(debug);
		cityControllerApi.getApiClient().setConnectTimeout(0);
		cityControllerApi.getApiClient().setReadTimeout(0);
		
		stateControllerApi = new StateControllerApi();
		stateControllerApi.getApiClient().setLenientOnJson(true);
		stateControllerApi.getApiClient().setDebugging(debug);
		stateControllerApi.getApiClient().setConnectTimeout(0);
		stateControllerApi.getApiClient().setReadTimeout(0);
	}

	@Produces
	public DeliveryControllerApi getDeliveryControllerApi() {
		return deliveryControllerApi;
	}

	@Produces
	public DemandControllerApi getDemandControllerApi() {
		return demandControllerApi;
	}

	@Produces
	public InternalOrderControllerApi getInternalOrderControllerApi() {
		return internalOrderControllerApi;
	}

	@Produces
	public InternalSupplyControllerApi getInternalSupplyControllerApi() {
		return internalSupplyControllerApi;
	}

	@Produces
	public OptimizatorControllerApi getOptimizatorControllerApi() {
		return optimizatorControllerApi;
	}

	@Produces
	public PartControllerApi getPartControllerApi() {
		return partControllerApi;
	}

	@Produces
	public SramControllerApi getSramControllerApi() {
		return sramControllerApi;
	}

	@Produces
	public AllocatedSramControllerApi getAllocatedSramControllerApi() {
		return allocatedSramControllerApi;
	}

	@Produces
	public ProcessingPartControllerApi getProcessingPartControllerApi() {
		return processingPartControllerApi;
	}

	@Produces
	public RemoteStationControllerApi getRemoteStationControllerApi() {
		return remoteStationControllerApi;
	}

	@Produces
	public RouteControllerApi getRouteControllerApi() {
		return routeControllerApi;
	}

	@Produces
	public StockControllerApi getStockControllerApi() {
		return stockControllerApi;
	}

	@Produces
	public ProductionControllerApi getProductionControllerApi() {
		return productionControllerApi;
	}

	@Produces
	public TacticalOptimizationControllerApi getTacticalOptimizationControllerApi() {
		return tacticalOptimizationControllerApi;
	}

	@Produces 
	public PredictionControllerApi getPredictiveControllerApi() {
		return predictiveControllerApi;
	}
	
	@Produces 
	public OperationalOptimizationControllerApi getOperationalOptimizationControllerApi() {
		return operationalOptimizationControllerApi;
	}
	
	@Produces 
	public DemandProjectedControllerApi getDemandProjectedControllerApi() {
		return demandProjectedControllerApi;
	}

	@Produces 
	public DemandProjectionStudyControllerApi getDemandProjectionStudyControllerApi() {
		return demandProjectionStudyControllerApi;
	}

	@Produces 
	public CityControllerApi getCityControllerApi() {
		return cityControllerApi;
	}
	
	@Produces 
	public StateControllerApi getStateControllerApi() {
		return stateControllerApi;
	}

	@Produces
	public OperationalOptimizationResultControllerApi getOperationalOptimizationResultControllerApi() {
		return operationalOptimizationResultControllerApi;
	}

	@Produces
	public TacticalOptimizationResultControllerApi getTacticalOptimizationResultControllerApi() {
		return tacticalOptimizationResultControllerApi;
	}
	
}
