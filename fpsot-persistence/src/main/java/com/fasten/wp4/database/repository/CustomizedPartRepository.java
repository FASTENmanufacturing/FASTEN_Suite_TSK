package com.fasten.wp4.database.repository;

interface CustomizedPartRepository {
	
	String[] retrievePartNamesByTacticalOptimizationOrderById(Long tacticalOptimizationId);

	int[] retrievePartIdsByTacticalOptimizationOrderById(Long tacticalOptimizationId);
	
}