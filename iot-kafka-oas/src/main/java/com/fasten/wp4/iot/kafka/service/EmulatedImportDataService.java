package com.fasten.wp4.iot.kafka.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasten.wp4.database.client.api.DeliveryControllerApi;
import com.fasten.wp4.database.client.api.DemandControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.ProcessingPartControllerApi;
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.api.SramControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.invoker.ApiResponse;
import com.fasten.wp4.database.client.model.Delivery;
import com.fasten.wp4.database.client.model.Demand;
import com.fasten.wp4.database.client.model.Part;
import com.fasten.wp4.database.client.model.ProcessingPart;
import com.fasten.wp4.database.client.model.RemoteStation;
import com.fasten.wp4.database.client.model.SRAM;
import com.fasten.wp4.iot.kafka.response.HistoricalDemandResponse;
import com.fasten.wp4.iot.kafka.response.HistoricalTimesResponse;
import com.fasten.wp4.iot.kafka.response.ProbabilityDristribuitionsResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmulatedImportDataService{

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private DeliveryControllerApi deliveryControllerApi;
	@Autowired
	private DemandControllerApi demandControllerApi;
	@Autowired
	private PartControllerApi partControllerApi;
	@Autowired
	private SramControllerApi SRAMControllerApi;
	@Autowired
	private ProcessingPartControllerApi processingPartControllerApi;
	@Autowired
	private RemoteStationControllerApi remoteStationControllerApi;
	@Autowired
	private ObjectMapper mapper;


	public void importData(HistoricalDemandResponse historicalDemandResponse ) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, ApiException, URISyntaxException {

		demand: {
		LOGGER.info("Importing data");
		List<Demand> demandsKafka = historicalDemandResponse.getDemands();
		LOGGER.info("Total demanded: " + demandsKafka.size());

		LOGGER.info("Importing parts from Demands");
		importParts(filterPartsFromDemand(demandsKafka));

		LOGGER.info("Importing remote stations from Demands");
		importRemoteStations(filterRemoteStationsFromDemand(demandsKafka));

		LOGGER.info("Importing demands");
		importDemands(demandsKafka);
	}

	delivery:{
		List<Delivery> deliveriesKafka = historicalDemandResponse.getDeliveries();
		LOGGER.info("Importing remote stations from Delivery");
		importRemoteStations(filterRemoteStationsFromDelivery(deliveriesKafka));

		LOGGER.info("Importing transport costs");
		importDeliveries(deliveriesKafka);
		LOGGER.info("Total of deliveries info: " + deliveriesKafka.size());
	}


	srams:{
		LOGGER.info("Importing producing resources info - SRAMS Models/Capacities");
		List<SRAM> SRAMsKafka= historicalDemandResponse.getSRAMs();
		importSRAMs(SRAMsKafka);
		LOGGER.info("Total of SRAMS info: " + SRAMsKafka.size());
	}

	processingParts:{

		List<ProcessingPart> processingPartsKafka= historicalDemandResponse.getProcessingParts();

		LOGGER.info("Importing parts from Processing Parts");
		importParts(filterPartsFromProcessingParts(processingPartsKafka));

		LOGGER.info("Importing SRAMs from Processing Parts");
		importSRAMs(filterSRAMsFromProcessingParts(processingPartsKafka));

		LOGGER.info("Importing Processing Parts info");
		importProcessingParts(processingPartsKafka);
		LOGGER.info("Total of Processing Parts info: " + processingPartsKafka.size());
	}

	LOGGER.info("End of Import data");

	}

	public void updateProbabilityDistribuitionsData(ProbabilityDristribuitionsResponse probabilityDristribuitionsResponse)  throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, ApiException, URISyntaxException {
	
		List<ProcessingPart> processingPartsKafka= probabilityDristribuitionsResponse.getProcessingParts();
		for (ProcessingPart processingPartKafka : processingPartsKafka) {
			ProcessingPart processingPart = processingPartControllerApi.getApiClient().getJSON().deserialize(mapper.writeValueAsString(processingPartKafka),ProcessingPart.class);
			saveOrUpdate(processingPart);
		}
	
	}

	public void updateHistoricalTimesData(HistoricalTimesResponse historicalTimesResponse)  throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, ApiException, URISyntaxException {
		List<ProcessingPart> processingPartsKafka= historicalTimesResponse.getProcessingParts();
		for (ProcessingPart processingPartKafka : processingPartsKafka) {
			ProcessingPart processingPart = processingPartControllerApi.getApiClient().getJSON().deserialize(mapper.writeValueAsString(processingPartKafka),ProcessingPart.class);
			saveOrUpdate(processingPart);
		}
	}

	private void importParts(List<Part> partsKafka) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, ApiException, URISyntaxException {

		for (Part partKafka : partsKafka) {
			Part part = partControllerApi.getApiClient().getJSON().deserialize(mapper.writeValueAsString(partKafka),Part.class);
			saveOrUpdate(part);
		}
	}

	private void importDemands(List<Demand> demandsKafka) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, ApiException, URISyntaxException {
	
		for (Demand demandKafka : demandsKafka) {
			Demand demand = demandControllerApi.getApiClient().getJSON().deserialize(mapper.writeValueAsString(demandKafka),Demand.class);
			saveOrUpdate(demand);
		}
	}

	private void importRemoteStations(List<RemoteStation> remotesStationsKafka) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, ApiException, URISyntaxException {
	
		for (RemoteStation remoteStationKafka : remotesStationsKafka) {
			RemoteStation remoteStation = remoteStationControllerApi.getApiClient().getJSON().deserialize(mapper.writeValueAsString(remoteStationKafka),RemoteStation.class);
			saveOrUpdate(remoteStation);
		}
	}

	private void importDeliveries(List<Delivery> deliveriesKafka) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, ApiException, URISyntaxException {
	
		for (Delivery deliveryKafka : deliveriesKafka) {
			Delivery delivery = deliveryControllerApi.getApiClient().getJSON().deserialize(mapper.writeValueAsString(deliveryKafka),Delivery.class);
			saveOrUpdate(delivery);
		}
	}

	private void importSRAMs(List<SRAM> SRAMsKafka) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, ApiException, URISyntaxException {
		for (SRAM SRAMKafka : SRAMsKafka) {
			SRAM SRAM = SRAMControllerApi.getApiClient().getJSON().deserialize(mapper.writeValueAsString(SRAMKafka),SRAM.class);
			saveOrUpdate(SRAM);
		}
	}

	private void importProcessingParts(List<ProcessingPart> processingPartsKafka) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException, ApiException, URISyntaxException {
	
		for (ProcessingPart processingPartKafka : processingPartsKafka) {
			ProcessingPart processingPart = processingPartControllerApi.getApiClient().getJSON().deserialize(mapper.writeValueAsString(processingPartKafka),ProcessingPart.class);
			saveOrUpdate(processingPart);
		}
	}

	private List<Part> filterPartsFromDemand(List<Demand> demandsKafka) {
		List<Part> parts = new ArrayList<>();
		for (Demand demandKafka : demandsKafka) {
			Part partKafka = demandKafka.getPart();
			if(!parts.contains(partKafka)) {
				parts.add(partKafka);
			}
		}
		return parts;
	}

	private List<RemoteStation> filterRemoteStationsFromDemand(List<Demand> demands) {
		List<RemoteStation> remotesStations = new ArrayList<>();
		for (Demand demandKafka : demands) {
			RemoteStation remoteStationKafka = demandKafka.getRemoteStation();
			if(!remotesStations.contains(remoteStationKafka)) {
				remotesStations.add(remoteStationKafka);
			}
		}
		return remotesStations;
	} 

	private List<RemoteStation> filterRemoteStationsFromDelivery(List<Delivery> deliveriesKafka) {
		List<RemoteStation> remotesStations = new ArrayList<>();
		for (Delivery deliveryKafka : deliveriesKafka) {
			RemoteStation remoteStationOriginKafka = deliveryKafka.getOrigin();
			RemoteStation remoteStationDestinationKafka = deliveryKafka.getDestination();
			if(!remotesStations.contains(remoteStationOriginKafka)) {
				remotesStations.add(remoteStationOriginKafka);
			}
			if(!remotesStations.contains(remoteStationDestinationKafka)) {
				remotesStations.add(remoteStationDestinationKafka);
			}
		}
		return remotesStations;
	}

	private List<Part> filterPartsFromProcessingParts(List<ProcessingPart> processingPartsKafka) {
		List<Part> parts = new ArrayList<>();
		for (ProcessingPart processingPartKafka : processingPartsKafka) {
			Part partKafka = processingPartKafka.getPart();
			if(!parts.contains(partKafka)) {
				parts.add(partKafka);
			}
		}
		return parts;
	}

	private List<SRAM> filterSRAMsFromProcessingParts(List<ProcessingPart> processingPartsKafka) {
		List<SRAM> SRAMs = new ArrayList<>();
		for (ProcessingPart processingPartKafka : processingPartsKafka) {
			SRAM printerKafka = processingPartKafka.getSram();
			if(!SRAMs.contains(printerKafka)) {
				SRAMs.add(printerKafka);
			}
		}
		return SRAMs;
	}

	private Part saveOrUpdate(Part part) throws ApiException, URISyntaxException {
		try{
			part.setId(partControllerApi.retrievePartByCode(part.getCode()).getId());
			partControllerApi.updatePartWithHttpInfo(part.getId(), part);
		}catch(ApiException e) {
			if(e.getCode()==404) {
				part.setId(getIdFromLocationHeader(partControllerApi.createPartWithHttpInfo(part)));
			}
		}
		return part;
	}

	private SRAM saveOrUpdate(SRAM SRAM) throws ApiException, URISyntaxException {
		try{
			SRAM.setId(SRAMControllerApi.retrieveSRAMByCode(SRAM.getCode()).getId());
			SRAMControllerApi.updateSRAMWithHttpInfo(SRAM.getId(), SRAM);
		}catch(ApiException e) {
			if(e.getCode()==404) {
				SRAM.setId(getIdFromLocationHeader(SRAMControllerApi.createSRAMWithHttpInfo(SRAM)));
			}
		}
		return SRAM;
	}

	private Demand saveOrUpdate(Demand demand) throws ApiException, URISyntaxException {

		demand.setRemoteStation(saveOrUpdate(demand.getRemoteStation()));
		demand.setPart(saveOrUpdate(demand.getPart()));

		try{
			demand.setId(demandControllerApi.retrieveDemandByCode(demand.getCode()).getId());
			demandControllerApi.updateDemandWithHttpInfo(demand.getId(), demand);
		}catch(ApiException e) {
			if(e.getCode()==404) {
				demand.setId(getIdFromLocationHeader(demandControllerApi.createDemandWithHttpInfo(demand)));
			}
		}
		return demand;
	}

	private ProcessingPart saveOrUpdate(ProcessingPart processingPart) throws ApiException, URISyntaxException {

		processingPart.setPart(saveOrUpdate(processingPart.getPart()));
		processingPart.setSram(saveOrUpdate(processingPart.getSram()));

		try{
			processingPart.setId((processingPartControllerApi.retrieveProcessingPartByPartCodeAndSRAMCode(processingPart.getSram().getCode(),processingPart.getPart().getCode())).getId());
			processingPartControllerApi.updateProcessingPartWithHttpInfo(processingPart.getId(), processingPart);
		}catch(ApiException e) {
			if(e.getCode()==404) {
				processingPart.setId(getIdFromLocationHeader(processingPartControllerApi.createProcessingPartWithHttpInfo(processingPart)));
			}
		}
		return processingPart;
	}

	private Delivery saveOrUpdate(Delivery delivery) throws ApiException, URISyntaxException {

		delivery.setOrigin(saveOrUpdate(delivery.getOrigin()));
		delivery.setDestination(saveOrUpdate(delivery.getDestination()));

		try{
			delivery.setId(deliveryControllerApi.retrieveDeliveryByRouteCode(delivery.getDestination().getCode(),delivery.getOrigin().getCode()).getId());
			deliveryControllerApi.updateDeliveryWithHttpInfo(delivery.getId(), delivery);
		}catch(ApiException e) {
			if(e.getCode()==404) {
				delivery.setId(getIdFromLocationHeader(deliveryControllerApi.createDeliveryWithHttpInfo(delivery)));
			}
		}
		return delivery;
	}

	private RemoteStation saveOrUpdate(RemoteStation remoteStation) throws ApiException, URISyntaxException {
		try{
			remoteStation.setId(remoteStationControllerApi.retrieveRemoteStationByCode(remoteStation.getCode()).getId());
			remoteStationControllerApi.updateRemoteStationWithHttpInfo(remoteStation.getId(), remoteStation);
		}catch(ApiException e) {
			if(e.getCode()==404) {
				remoteStation.setId(getIdFromLocationHeader(remoteStationControllerApi.createRemoteStationWithHttpInfo(remoteStation)));
			}
		}
		return remoteStation;
	}

	private long getIdFromLocationHeader(ApiResponse<Object> apiResponse) throws URISyntaxException {
		URI uri = new URI(apiResponse.getHeaders().get("Location").get(0));
		String path = uri.getPath();
		String idStr = path.substring(path.lastIndexOf('/') + 1);
		long id = Long.parseLong(idStr);
		return id;
	}

}
