package com.fasten.wp4.database.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TacticalOptimizationType {
	@JsonProperty("Number of facilites")
	NumberOfFacilites{@Override public String toString() {return "Number of facilites";}},
	@JsonProperty("Cost Benefit")
	CostBenefit{@Override public String toString() {return "Cost Benefit";}}, 
	@JsonProperty("Analysis")
	Analysis{@Override public String toString() {return "Analysis";}} 
}
