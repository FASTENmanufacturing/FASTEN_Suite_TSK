package com.fasten.wp4.optimizator.tactical.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasten.wp4.database.model.Delivery;
import com.fasten.wp4.database.model.Demand;
import com.fasten.wp4.database.model.DistributionCenter;
import com.fasten.wp4.database.model.TacticalOptimization;
import com.fasten.wp4.database.model.TacticalOptimizationResult;
import com.fasten.wp4.database.repository.DeliveryRepository;
import com.fasten.wp4.database.repository.DemandRepository;
import com.fasten.wp4.database.repository.DistributionCenterRepository;
import com.fasten.wp4.database.repository.PartRepository;
import com.fasten.wp4.database.repository.ProcessingPartRepository;
import com.fasten.wp4.database.repository.TacticalOptimizationRepository;
import com.fasten.wp4.database.repository.TacticalOptimizationResultRepository;
import com.fasten.wp4.optimizator.tactical.cplex.versions.PMedianPartsPrintersComplexTskMatrix;
import com.fasten.wp4.optimizator.tactical.cplex.versions.PMedianPartsPrintersSimpleTskMatrix;
import com.fasten.wp4.optimizator.tactical.cplex.versions.PMedianPartsTskMatrix;
import com.fasten.wp4.optimizator.tactical.cplex.versions.PMedianTskCollection;
import com.fasten.wp4.optimizator.tactical.cplex.versions.PMedianTskMatrix;
import com.fasten.wp4.optimizator.tactical.model.CplexResult;

import io.swagger.annotations.ApiOperation;


@ConditionalOnProperty(prefix="controller.versions", name="enabled")
@RestController("/execute/versions")
public class TacticalOptimizationVersionsController {

	@Autowired
	private TacticalOptimizationRepository tacticalOptimizationRepository;

	@Autowired
	private DemandRepository demandRepository;

	@Autowired
	private DistributionCenterRepository distributionCenterRepository;

	@Autowired
	private PartRepository partRepository;

	@Autowired
	private DeliveryRepository deliveryRepository;

	@Autowired
	private ProcessingPartRepository processingPartRepository;

	@Autowired
	private TacticalOptimizationResultRepository tacticalOptimizationResultRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@GetMapping("/pMedianPartsPrintersComplexTskMatrix")
	@ApiOperation(nickname="pMedianPartsPrintersComplexTskMatrix", value = "execute PMedianPartsPrintersComplexTskMatrix")
	@Transactional
	public ResponseEntity<HashMap<String, Object>> pMedianPartsPrintersComplexTskMatrix(@RequestParam Long tacticalOptimizationId) {
		Instant start_time = Instant.now();
		LOGGER.info("Starting optimization");
		LOGGER.info("Getting data");
		TacticalOptimization tacticalOptimizationConfig = tacticalOptimizationRepository.findById(tacticalOptimizationId).get();
		
		int[] distributionCenters = distributionCenterRepository.retrieveDistributionCenterIdsByTacticalOptimizationOrderById(tacticalOptimizationId);
		String[] distributionCentersNames = distributionCenterRepository.retrieveDistributionCenterNamesByTacticalOptimizationOrderById(tacticalOptimizationId);
		int[] remoteStations = distributionCenters;
		String[] remoteStationsNames = distributionCentersNames;
		int[] parts = partRepository.retrievePartIdsByTacticalOptimizationOrderById(tacticalOptimizationId);
		String[] partsNames =partRepository.retrievePartNamesByTacticalOptimizationOrderById(tacticalOptimizationId);
		int[][] matrix = deliveryRepository.retrieveTimeMatrix(distributionCenters, remoteStations);
		double[] processingTime =processingPartRepository.retrieveProcessingPartsByTacticalOptimizationOrderById(tacticalOptimizationId);
		int[][] demandByPart=demandRepository.retrieveDemandPartsByTacticalOptimizationOrderByDistributionCenterId(tacticalOptimizationId);
		int[][] numberOfOrderByPart = demandRepository.retrieveDemandOrdersPartsByTacticalOptimizationOrderByDistributionCenterId(tacticalOptimizationId);
		
		int sramCapacity = tacticalOptimizationConfig.getSramCapacity().intValueExact(); //seconds/day
		PMedianPartsPrintersComplexTskMatrix model = new PMedianPartsPrintersComplexTskMatrix(tacticalOptimizationConfig, distributionCenters, distributionCentersNames, remoteStations, remoteStationsNames, parts, partsNames, processingTime, numberOfOrderByPart, demandByPart, matrix, sramCapacity);
		HashMap<String, Object> result = model.costBenefit();
		Instant stop_time = Instant.now();
		LOGGER.info("Spent time:"+ChronoUnit.MILLIS.between(start_time, stop_time)+" ms");
		
		LOGGER.info("Saving optimization results do database");
		tacticalOptimizationResultRepository.deleteByStudy(tacticalOptimizationConfig);
		TacticalOptimizationResult tacticalOptimizationResult = (TacticalOptimizationResult) result.get("tacticalOptimizationResult");
		tacticalOptimizationResultRepository.save(tacticalOptimizationResult);
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/pMedianPartsPrintersSimpleTskMatrix")
	@ApiOperation(nickname="pMedianPartsPrintersSimpleTskMatrix", value = "execute PMedianPartsPrintersSimpleTskMatrix (main version)")
	@Transactional
	public ResponseEntity<HashMap<String, Object>> pMedianPartsPrintersSimpleTskMatrix(@RequestParam Long tacticalOptimizationId) {
		Instant start_time = Instant.now();
		LOGGER.info("Starting optimization");
		LOGGER.info("Getting data");
		TacticalOptimization tacticalOptimizationConfig = tacticalOptimizationRepository.findById(tacticalOptimizationId).get();
		
		int[] distributionCenters = distributionCenterRepository.retrieveDistributionCenterIdsByTacticalOptimizationOrderById(tacticalOptimizationId);
		String[] distributionCentersNames = distributionCenterRepository.retrieveDistributionCenterNamesByTacticalOptimizationOrderById(tacticalOptimizationId);
		int[] remoteStations = distributionCenters;
		String[] remoteStationsNames = distributionCentersNames;
		int[] parts = partRepository.retrievePartIdsByTacticalOptimizationOrderById(tacticalOptimizationId);
		String[] partsNames =partRepository.retrievePartNamesByTacticalOptimizationOrderById(tacticalOptimizationId);
		int[][] matrix = deliveryRepository.retrieveTimeMatrix(distributionCenters, remoteStations);
		double[] processingTime =processingPartRepository.retrieveProcessingPartsByTacticalOptimizationOrderById(tacticalOptimizationId);
		int[][] demandByPart=demandRepository.retrieveDemandPartsByTacticalOptimizationOrderByDistributionCenterId(tacticalOptimizationId);
		int[][] numberOfOrderByPart = demandRepository.retrieveDemandOrdersPartsByTacticalOptimizationOrderByDistributionCenterId(tacticalOptimizationId);
		
		int sramCapacity = tacticalOptimizationConfig.getSramCapacity().intValueExact(); //seconds/day
		PMedianPartsPrintersSimpleTskMatrix model = new PMedianPartsPrintersSimpleTskMatrix(tacticalOptimizationConfig, distributionCenters, distributionCentersNames, remoteStations, remoteStationsNames, parts, partsNames, processingTime, numberOfOrderByPart, demandByPart, matrix, sramCapacity);
		HashMap<String, Object> result = model.costBenefit();
		Instant stop_time = Instant.now();
		LOGGER.info("Spent time:"+ChronoUnit.MILLIS.between(start_time, stop_time)+" ms");
		
		LOGGER.info("Saving optimization results do database");
		tacticalOptimizationResultRepository.deleteByStudy(tacticalOptimizationConfig);
		TacticalOptimizationResult tacticalOptimizationResult = (TacticalOptimizationResult) result.get("tacticalOptimizationResult");
		tacticalOptimizationResultRepository.save(tacticalOptimizationResult);
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/pMedianPartsTskMatrix")
	@ApiOperation(nickname="pMedianPartsTskMatrix", value = "execute pMedianPartsTskMatrix")
	@Transactional
	public ResponseEntity<Entry<CplexResult, TacticalOptimizationResult>> pMedianPartsTskMatrix(@RequestParam Long tacticalOptimizationId) {
		Instant start_time = Instant.now();
		LOGGER.info("Starting optimization");
		LOGGER.info("Getting data");
		TacticalOptimization tacticalOptimizationConfig = tacticalOptimizationRepository.findById(tacticalOptimizationId).get();
		
		int[] distributionCenters = distributionCenterRepository.retrieveDistributionCenterIdsByTacticalOptimizationOrderById(tacticalOptimizationId);
		String[] distributionCentersNames = distributionCenterRepository.retrieveDistributionCenterNamesByTacticalOptimizationOrderById(tacticalOptimizationId);
		int[] remoteStations = distributionCenters;
		String[] remoteStationsNames = distributionCentersNames;
		int[] parts = partRepository.retrievePartIdsByTacticalOptimizationOrderById(tacticalOptimizationId);
		String[] partsNames =partRepository.retrievePartNamesByTacticalOptimizationOrderById(tacticalOptimizationId);
		int[][] matrix = deliveryRepository.retrieveTimeMatrix(distributionCenters, remoteStations);
		double[] processingTime =processingPartRepository.retrieveProcessingPartsByTacticalOptimizationOrderById(tacticalOptimizationId);
		int[][] demandByPart=demandRepository.retrieveDemandPartsByTacticalOptimizationOrderByDistributionCenterId(tacticalOptimizationId);
		int[][] numberOfOrder = demandRepository.retrieveDemandOrdersPartsByTacticalOptimizationOrderByDistributionCenterId(tacticalOptimizationId);
		
		PMedianPartsTskMatrix model = new PMedianPartsTskMatrix(tacticalOptimizationConfig, distributionCenters, distributionCentersNames, remoteStations, remoteStationsNames, parts, partsNames, processingTime, numberOfOrder, demandByPart, matrix);
		Entry<CplexResult, TacticalOptimizationResult> result = model.costBenefit();
		Instant stop_time = Instant.now();
		LOGGER.info("Spent time:"+ChronoUnit.MILLIS.between(start_time, stop_time)+" ms");
		
		LOGGER.info("Saving optimization results do database");
		tacticalOptimizationResultRepository.deleteByStudy(tacticalOptimizationConfig);
		result.setValue(tacticalOptimizationResultRepository.save(result.getValue()));
		return ResponseEntity.ok(result);
	}

	@GetMapping("/pMedianTskCollection")
	@ApiOperation(nickname="pMedianTskCollection", value = "execute PMedianTskCollection")
	@Transactional
	public ResponseEntity<CplexResult> pMedianTskCollection(@RequestParam Long tacticalOptimizationId) {
		Instant start_time = Instant.now();
		LOGGER.info("Starting optimization");
		LOGGER.info("Getting data");
		TacticalOptimization tacticalOptimization = tacticalOptimizationRepository.findById(tacticalOptimizationId).get();
		List<Demand> demands = demandRepository.retrieveDemandByTacticalOptimizationOrderByDistributionCenterName(tacticalOptimizationId);
		List<DistributionCenter> distributionCenters = distributionCenterRepository.retrieveDistributionCenterByTacticalOptimizationOrderByName(tacticalOptimizationId);
		List<DistributionCenter> remoteStations = distributionCenters;
		List<Delivery> deliveries = deliveryRepository.retrieveDeliveryMatrixByTacticalOptimizationOrderByName(tacticalOptimizationId);
		PMedianTskCollection model = new PMedianTskCollection(tacticalOptimization,distributionCenters, remoteStations, demands, deliveries);
		CplexResult result = model.run();
		Instant stop_time = Instant.now();
		LOGGER.info("Spent time:"+ChronoUnit.MILLIS.between(start_time, stop_time)+" ms");
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/pMedianTskMatrix")
	@ApiOperation(nickname="pMedianTskMatrix", value = "execute PMedianTskMatrix")
	@Transactional
	public ResponseEntity<Entry<CplexResult, TacticalOptimizationResult>> pMedianTskMatrix(@RequestParam Long tacticalOptimizationId) {
		Instant start_time = Instant.now();
		LOGGER.info("Starting optimization");
		LOGGER.info("Getting data");
		TacticalOptimization tacticalOptimizationConfig = tacticalOptimizationRepository.findById(tacticalOptimizationId).get();
		int[] demands = demandRepository.retrieveDemandOrdersByTacticalOptimizationOrderByDistributionCenterId(tacticalOptimizationId);
		int[] distributionCenters = distributionCenterRepository.retrieveDistributionCenterIdsByTacticalOptimizationOrderById(tacticalOptimizationId);
		String[] distributionCentersNames = distributionCenterRepository.retrieveDistributionCenterNamesByTacticalOptimizationOrderById(tacticalOptimizationId);
		int[] remoteStations = distributionCenters;
		String[] remoteStationsNames = distributionCentersNames;
		int[][] matrix = deliveryRepository.retrieveTimeMatrix(distributionCenters, remoteStations);
		PMedianTskMatrix model = new PMedianTskMatrix(tacticalOptimizationConfig, distributionCenters, distributionCentersNames, remoteStations, remoteStationsNames, demands, matrix);
		Entry<CplexResult, TacticalOptimizationResult> result = model.costBenefit();
		Instant stop_time = Instant.now();
		LOGGER.info("Spent time:"+ChronoUnit.MILLIS.between(start_time, stop_time)+" ms");
		LOGGER.info("Saving optimization results do database");
		tacticalOptimizationResultRepository.deleteByStudy(tacticalOptimizationConfig);
		result.setValue(tacticalOptimizationResultRepository.save(result.getValue()));
		return ResponseEntity.ok(result);
	}

}
