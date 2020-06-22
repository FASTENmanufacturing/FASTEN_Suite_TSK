package com.fasten.wp4.optimizator.tactical.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasten.wp4.optimizator.tactical.cplex.samples.PMedianCollection;
import com.fasten.wp4.optimizator.tactical.cplex.samples.PMedianMatrix;
import com.fasten.wp4.optimizator.tactical.cplex.samples.PMedianMatrixObjectResult;
import com.fasten.wp4.optimizator.tactical.cplex.samples.Sample1;
import com.fasten.wp4.optimizator.tactical.cplex.samples.Sample2;
import com.fasten.wp4.optimizator.tactical.cplex.samples.Sample3;
import com.fasten.wp4.optimizator.tactical.model.CplexResult;

@ConditionalOnProperty(prefix="controller.samples", name="enabled")
@RestController("/execute/samples")
public class TacticalOptimizationSamplesController {

	@SuppressWarnings("unused")
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@GetMapping("/sample1")
	public void sample1() {
		Sample1 sample = new Sample1();
		sample.run();
	}

	@GetMapping("/sample2")
	public void sample2() {
		Sample2 sample = new Sample2();
		sample.run();
	}
	
	@GetMapping("/sample3")
	public void sample3(@RequestParam int n) {
		Sample3 sample = new Sample3();
		sample.run(n);
	}

	@GetMapping("/pMedianCollection")
	public ResponseEntity<CplexResult> pMedianCollection() {
		PMedianCollection pmedian = new PMedianCollection();
		CplexResult result = pmedian.run();
		return ResponseEntity.ok(result);
	}

	@GetMapping("/pMedianMatrix")
	public ResponseEntity<CplexResult> pMedianMatrix() {
		PMedianMatrix pmedian = new PMedianMatrix();
		CplexResult result = pmedian.run();
		return ResponseEntity.ok(result);
	}

	@GetMapping("/pMedianMatrixObjectResult")
	public void pMedianMatrixObjectResult() {
		PMedianMatrixObjectResult pmedian = new PMedianMatrixObjectResult();
		pmedian.solveMe();
	}

}
