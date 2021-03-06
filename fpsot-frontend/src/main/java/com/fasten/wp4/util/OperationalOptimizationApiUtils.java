package com.fasten.wp4.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import com.fasten.wp4.database.client.model.OperationalOptimizationResult;
import com.fasten.wp4.optimizator.operational.client.model.AllocationResult;
import com.fasten.wp4.optimizator.operational.client.model.AllocationResultOptimizationResult;

public class OperationalOptimizationApiUtils {
	
	
//	#############################################
//	################ OUTPUT #####################
//	#############################################
	public static List<OperationalOptimizationResult> convertOutput(AllocationResult allocationResult) {
		List<OperationalOptimizationResult> result = new ArrayList<OperationalOptimizationResult>();
		for (AllocationResultOptimizationResult allocation : allocationResult.getOptimizationResult()) {
			OperationalOptimizationResult operationalOptimizationResult = new OperationalOptimizationResult();
			operationalOptimizationResult.setQueueTime(allocation.getQueueTime());
			operationalOptimizationResult.setTimeProduction(allocation.getTimeProduction());
			operationalOptimizationResult.setTimeTravel(allocation.getTimeTravel());
			operationalOptimizationResult.setTotalTime(allocation.getTotalTime());
		}
		return result;
	}
	
	public static String unaccent(String src) {
		return Normalizer
				.normalize(src, Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "");
	}

	

	
}