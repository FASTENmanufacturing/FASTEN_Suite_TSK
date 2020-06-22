package com.fasten.wp4.database.repository;

interface CustomizedDistributionCenterRepository {

	String[] retrieveDistributionCenterNamesByTacticalOptimizationOrderById(Long tacticalOptimizationId);

	int[] retrieveDistributionCenterIdsByTacticalOptimizationOrderById(Long tacticalOptimizationId);
	
}