package com.fasten.wp4.fpsot.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasten.wp4.database.client.api.CityControllerApi;
import com.fasten.wp4.database.client.api.OperationalOptimizationControllerApi;
import com.fasten.wp4.database.client.api.OperationalOptimizationResultControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.SramControllerApi;
import com.fasten.wp4.database.client.invoker.ApiResponse;
import com.fasten.wp4.database.client.model.Address;
import com.fasten.wp4.database.client.model.City;
import com.fasten.wp4.database.client.model.OperationalOptimization;
import com.fasten.wp4.database.client.model.OperationalOptimizationResult;
import com.fasten.wp4.database.client.model.Part;
import com.fasten.wp4.fpsot.event.WebHookEvent;
import com.fasten.wp4.optimizator.operational.client.api.DefaultApi;
import com.fasten.wp4.optimizator.operational.client.model.AllocationResult;
import com.fasten.wp4.optimizator.operational.client.model.AllocationResultOptimizationResult;
import com.fasten.wp5.fastengateway.client.model.Demand;
import com.fasten.wp5.fastengateway.client.model.DemandOrigin;
import com.fasten.wp5.fastengateway.client.model.OrderOptimization;
import com.fasten.wp5.fastengateway.client.model.OrderOptimizationList;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController("/HSO")
public class HSOController {

	@Autowired
	private PartControllerApi partControllerApi;
	@Autowired
	private CityControllerApi cityControllerApi;
	@Autowired
	private SramControllerApi sramControllerApi;
	@Autowired
	private OperationalOptimizationControllerApi operationalOptimizationControllerApi;
	@Autowired
	private OperationalOptimizationResultControllerApi operationalOptimizationResultControllerApi;
	
	@Autowired
	private DefaultApi optimizatorOperationalApi;
	
	@Autowired
	private com.fasten.wp5.fastengateway.client.api.DefaultApi fastenGatewayApi;
	
	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;
	
	@PostMapping("/operationalOptimization/demand")
	@ApiOperation(nickname="execute", value = "Configures and execute a operational optimization", notes = "Also returns the url to created data in header location ")
	public  ResponseEntity<AllocationResult> execute(	@ApiParam(value = "If this request must be forwarded to Fasten Gateway") 
														@RequestParam(value = "forwardToFastenGateway",defaultValue = "false", required = false) Boolean forwardToFastenGateway,
														@RequestBody Demand demand) throws Exception {

		//new study
		OperationalOptimization operationalOptimization = new OperationalOptimization();
		
		//set address
		Address address = new Address();
		DemandOrigin demandOrigin = demand.getOrigin();
		try{
			City city = cityControllerApi.retrieveCityByName(demandOrigin.getCity());
			address.setCity(city);
			address.setState(city.getState());
		}catch(Exception e) {}
		address.setComplement(demandOrigin.getComplement());
		address.setCountry(demandOrigin.getCountry());
		try {
			address.setLatitude(demandOrigin.getLatitude().doubleValue());
			address.setLongitude(demandOrigin.getLongitude().doubleValue());
		}catch(Exception e) {}
		address.setNeighborhood(demandOrigin.getNeighborhood());
		address.setNumber(demandOrigin.getNumber());
		address.setStreet(demandOrigin.getStreet());
		address.setZipcode(demandOrigin.getZipcode());
		operationalOptimization.address(address);

		//set part
		try {
			Part part = partControllerApi.retrievePartByName(demand.getPart().getDescription());
			operationalOptimization.setPart(part);
		}catch(Exception e) {}
		
		//set quantity
		operationalOptimization.setQuantity(demand.getQuantity());
		
		//set orderID
		operationalOptimization.setOrderID(demand.getOrderID());
		
		//save
		operationalOptimization.setId(getIdFromLocationHeader(operationalOptimizationControllerApi.createOperationalOptimizationWithHttpInfo(operationalOptimization)));
		
		//database-oas
		List<OperationalOptimizationResult> results = new ArrayList<OperationalOptimizationResult>();
		
		//fastengateway
		List<OrderOptimization> optimizationResults = new ArrayList<OrderOptimization>();
		
		String latitude = "";
		String longitude = "";
		if(operationalOptimization.getAddress().getLatitude()!=null && operationalOptimization.getAddress().getLongitude()!=null) {
			latitude=operationalOptimization.getAddress().getLatitude().toString();
			longitude=operationalOptimization.getAddress().getLongitude().toString();
		}else {
			latitude=operationalOptimization.getAddress().getCity().getLatitude().toString();
			longitude=operationalOptimization.getAddress().getCity().getLongitude().toString();
		}
		
		AllocationResult allocateResult = optimizatorOperationalApi.allocatePost(demand.getOrderID(), longitude,latitude, operationalOptimization.getQuantity().toString(), operationalOptimization.getPart().getName());
		for (AllocationResultOptimizationResult allocation : allocateResult.getOptimizationResult()) {
			if(allocation.getSRAM()!=null) {
				OperationalOptimizationResult operationalOptimizationResult = new OperationalOptimizationResult();
				OrderOptimization orderOptimization = new OrderOptimization();
				
				operationalOptimizationResult.setQueueTime(allocation.getQueueTime());
				orderOptimization.setQueueTime(allocation.getQueueTime());
				
				operationalOptimizationResult.setTimeProduction(allocation.getTimeProduction());
				orderOptimization.setTimeProduction(allocation.getTimeProduction());
				
				operationalOptimizationResult.setTimeTravel(allocation.getTimeTravel());
				orderOptimization.setTimeTravel(allocation.getTimeTravel());
				
				operationalOptimizationResult.setTotalTime(allocation.getTotalTime());
				orderOptimization.setTotalTime(allocation.getTotalTime());
				
				operationalOptimizationResult.setSram(sramControllerApi.retrieveSRAMByCode(allocation.getSRAM()));
				orderOptimization.setSRAM(allocation.getSRAM());
				
				operationalOptimizationResult.setStudy(operationalOptimization);

				results.add(operationalOptimizationResult);
				optimizationResults.add(orderOptimization);
			}
		}
		operationalOptimizationResultControllerApi.createOperationalOptimizationResults(results);
		
		Map<String, Object> payload = new HashMap<String, Object>();
		payload.put("orderID", demand.getOrderID());
//		payload.put("optimizationResults", results);
		WebHookEvent operationalOptimizationFinishedEvent = new WebHookEvent(WebHookEvent.OPERATIONAL_OPTIMIZATION_FINISHED, payload);
		applicationEventPublisher.publishEvent(operationalOptimizationFinishedEvent);
		
		OrderOptimizationList orderOptimizationList = new OrderOptimizationList();
		orderOptimizationList.setOptimizationResults(optimizationResults);
		
		if(forwardToFastenGateway) {
			fastenGatewayApi.postRestOrderOptimizationItem(demand.getOrderID(), orderOptimizationList);
		}
		
		return ResponseEntity.ok(allocateResult);
		
	}
	

	@PostMapping("/operationalOptimization")
	@ApiOperation(nickname="executeOperationalOptimization", value = "Configures and execute a operational optimization", notes = "Also returns the url to created data in header location ")
	public  ResponseEntity<AllocationResult> executeOperationalOptimization(
				
				@ApiParam(value = "Production Order Indentification")
				@RequestParam(value = "orderID", required = false) String orderID,
				
				@ApiParam(value = "The delivery location")
				@RequestParam(value = "origin", required = false) String origin,
				
				@ApiParam(value = "The quantity to be allocate")
				@RequestParam(value = "quantity", required = false) String quantity,
				
				@ApiParam(value = "The type of spare part (ex.: Button, Dosing chamber, Grid Air Condition, Support for escalator, Home Lift frame)") 
				@RequestParam(value = "part", required = false) String partName, 
				
				@ApiParam(value = "If this request must be forwarded to Fasten Gateway") 
				@RequestParam(value = "forwardToFastenGateway",defaultValue = "false", required = false) Boolean forwardToFastenGateway
				
			) throws Exception {
		
		//new study
		OperationalOptimization operationalOptimization = new OperationalOptimization();
		
		//set address
		Address address = new Address();
		try{
			City city = cityControllerApi.retrieveCityByName(origin);
			address.setCity(city);
			address.setState(city.getState());
		}catch(Exception e) {}
		operationalOptimization.address(address);

		//set part
		try {
			Part part = partControllerApi.retrievePartByName(partName);
			operationalOptimization.setPart(part);
		}catch(Exception e) {}
		
		//set quantity
		operationalOptimization.setQuantity(Integer.parseInt(quantity));
		
		//set orderID
		operationalOptimization.setOrderID(orderID);
		
		//save
		operationalOptimization.setId(getIdFromLocationHeader(operationalOptimizationControllerApi.createOperationalOptimizationWithHttpInfo(operationalOptimization)));
		
		//database-oas
		List<OperationalOptimizationResult> results = new ArrayList<OperationalOptimizationResult>();
		
		//fastengateway
		List<OrderOptimization> optimizationResults = new ArrayList<OrderOptimization>();
		
		String latitude = "";
		String longitude = "";
		if(operationalOptimization.getAddress().getLatitude()!=null && operationalOptimization.getAddress().getLongitude()!=null) {
			latitude=operationalOptimization.getAddress().getLatitude().toString();
			longitude=operationalOptimization.getAddress().getLongitude().toString();
		}else {
			latitude=operationalOptimization.getAddress().getCity().getLatitude().toString();
			longitude=operationalOptimization.getAddress().getCity().getLongitude().toString();
		}
		
		AllocationResult allocateResult = optimizatorOperationalApi.allocatePost(orderID, longitude,latitude, operationalOptimization.getQuantity().toString(), operationalOptimization.getPart().getName());
		for (AllocationResultOptimizationResult allocation : allocateResult.getOptimizationResult()) {
			if(allocation.getSRAM()!=null) {
				OperationalOptimizationResult operationalOptimizationResult = new OperationalOptimizationResult();
				OrderOptimization orderOptimization = new OrderOptimization();
				
				operationalOptimizationResult.setQueueTime(allocation.getQueueTime());
				orderOptimization.setQueueTime(allocation.getQueueTime());
				
				operationalOptimizationResult.setTimeProduction(allocation.getTimeProduction());
				orderOptimization.setTimeProduction(allocation.getTimeProduction());
				
				operationalOptimizationResult.setTimeTravel(allocation.getTimeTravel());
				orderOptimization.setTimeTravel(allocation.getTimeTravel());
				
				operationalOptimizationResult.setTotalTime(allocation.getTotalTime());
				orderOptimization.setTotalTime(allocation.getTotalTime());
				
				operationalOptimizationResult.setSram(sramControllerApi.retrieveSRAMByCode(allocation.getSRAM()));
				orderOptimization.setSRAM(allocation.getSRAM());
				
				operationalOptimizationResult.setStudy(operationalOptimization);

				results.add(operationalOptimizationResult);
				optimizationResults.add(orderOptimization);
			}
		}
		operationalOptimizationResultControllerApi.createOperationalOptimizationResults(results);
		
		//WebHook event
		Map<String, Object> payload = new HashMap<String, Object>();
		payload.put("orderID", orderID);
//		payload.put("optimizationResults", results);
		WebHookEvent operationalOptimizationFinishedEvent = new WebHookEvent(WebHookEvent.OPERATIONAL_OPTIMIZATION_FINISHED, payload);
		applicationEventPublisher.publishEvent(operationalOptimizationFinishedEvent);
		
		OrderOptimizationList orderOptimizationList = new OrderOptimizationList();
		orderOptimizationList.setOptimizationResults(optimizationResults);
		
		if(forwardToFastenGateway) {
			fastenGatewayApi.postRestOrderOptimizationItem(orderID, orderOptimizationList);
		}
		
		return ResponseEntity.ok(allocateResult);
		
	}
	
	private long getIdFromLocationHeader(ApiResponse<Object> apiResponse) throws URISyntaxException {
		URI uri = new URI(apiResponse.getHeaders().get("Location").get(0));
		String path = uri.getPath();
		String idStr = path.substring(path.lastIndexOf('/') + 1);
		long id = Long.parseLong(idStr);
		return id;
	}
	
}
