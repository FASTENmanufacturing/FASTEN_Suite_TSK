package com.fasten.wp4.database.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TacticalOptimizationStatus {
	@JsonProperty("Valid")
	Valid{@Override public String toString() {return "Valid";}},
	@JsonProperty("Invalid")
	Invalid{@Override public String toString() {return "Invalid";}}
}
