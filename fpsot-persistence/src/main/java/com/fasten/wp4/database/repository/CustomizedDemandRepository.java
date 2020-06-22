package com.fasten.wp4.database.repository;

interface CustomizedDemandRepository {
	
	int[] retrieveDemandOrdersByTacticalOptimizationOrderByDistributionCenterId(Long tacticalOptimizationId);
	
	int[][] retrieveDemandPartsByTacticalOptimizationOrderByDistributionCenterId(Long tacticalOptimizationId);

	int[][] retrieveDemandOrdersPartsByTacticalOptimizationOrderByDistributionCenterId(Long tacticalOptimizationId);
}