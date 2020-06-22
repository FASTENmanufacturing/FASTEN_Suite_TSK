package com.fasten.wp4.database.repository;

interface CustomizedProcessingPartRepository {
	
	double[] retrieveProcessingPartsByTacticalOptimizationOrderById(Long tacticalOptimizationId);
	
}