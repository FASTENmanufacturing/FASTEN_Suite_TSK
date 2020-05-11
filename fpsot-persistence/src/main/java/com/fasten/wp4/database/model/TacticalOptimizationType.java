package com.fasten.wp4.database.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TacticalOptimizationType {
	@JsonProperty("Lead Time")
	LeadTime{@Override public String toString() {return "Lead Time";}},
	@JsonProperty("Cost Benefit")
	CostBenefit{@Override public String toString() {return "Cost Benefit";}}
}
