package com.fasten.wp4.optimizator.tactical.model;

import java.util.ArrayList;
import java.util.List;

import com.fasten.wp4.database.model.TacticalOptimizationResult;
import com.fasten.wp4.database.model.TacticalOptimizationResult;

public class TacticalOptimizationResponse {

	private CplexResult cplexResult;
	private TacticalOptimizationResult tacticalOptimizationResult;
	private List<TacticalOptimizationResult> tacticalOptimizationResults;

	public TacticalOptimizationResponse() {
	}

	public TacticalOptimizationResponse(CplexResult cplexResult,
			TacticalOptimizationResult tacticalOptimizationResult) {
		super();
		this.cplexResult = cplexResult;
		this.tacticalOptimizationResult = tacticalOptimizationResult;
	}

	public CplexResult getCplexResult() {
		return cplexResult;
	}

	public void setCplexResult(CplexResult cplexResult) {
		this.cplexResult = cplexResult;
	}

	public TacticalOptimizationResult getTacticalOptimizationResult() {
		return tacticalOptimizationResult;
	}

	public void setTacticalOptimizationResult(TacticalOptimizationResult tacticalOptimizationResult) {
		this.tacticalOptimizationResult = tacticalOptimizationResult;
	}

	public List<TacticalOptimizationResult> getTacticalOptimizationResults() {
		return tacticalOptimizationResults;
	}

	public void setTacticalOptimizationResults(List<TacticalOptimizationResult> tacticalOptimizationResults) {
		this.tacticalOptimizationResults = tacticalOptimizationResults;
	}
	
	public TacticalOptimizationResult addTacticalOptimizationResults(TacticalOptimizationResult tacticalOptimizationResult) {
		if (getTacticalOptimizationResults() == null) {
			setTacticalOptimizationResults(new ArrayList<>());
		}
		getTacticalOptimizationResults().add(tacticalOptimizationResult);
		return tacticalOptimizationResult;
	}
	
	public TacticalOptimizationResult removeTacticalOptimizationResults(TacticalOptimizationResult tacticalOptimizationResult) {
		if (getTacticalOptimizationResults() == null) {
			return tacticalOptimizationResult;
		}
		getTacticalOptimizationResults().remove(tacticalOptimizationResult);
		return tacticalOptimizationResult;
	}

}
