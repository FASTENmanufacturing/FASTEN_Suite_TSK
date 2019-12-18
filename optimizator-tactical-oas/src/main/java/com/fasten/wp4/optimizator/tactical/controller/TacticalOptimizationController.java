package com.fasten.wp4.optimizator.tactical.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasten.wp4.optimizator.tactical.model.TacticalOptimization;
import com.fasten.wp4.optimizator.tactical.model.TacticalOptimizationResult;
import com.fasten.wp4.optimizator.tactical.repository.TacticalOptimizationRepository;

import io.swagger.annotations.ApiOperation;

@RestController
public class TacticalOptimizationController {

	@Autowired
	private TacticalOptimizationRepository repository;

	@PostMapping("/execute")
	@ApiOperation(nickname="executeTacticalOptimization", value = "Execute a tactical optimization")
	public ResponseEntity<TacticalOptimizationResult> executeTacticalOptimization(@RequestBody TacticalOptimization tacticalOptimization) {
		
		try {Thread.sleep(30000);} catch (InterruptedException e) {}
		
		return ResponseEntity.ok(new TacticalOptimizationResult());
	}
	
}
