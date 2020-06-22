package com.fasten.wp4.optimizator.tactical.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
	@JsonProperty("Error")
	Error{@Override public String toString() {return "Error";}},
	@JsonProperty("Unknown")
	Unknown{@Override public String toString() {return "Unknown";}},
	@JsonProperty("Bounded")
	Bounded{@Override public String toString() {return "Bounded";}},
	@JsonProperty("Optimal")
	Optimal{@Override public String toString() {return "Optimal";}},
	@JsonProperty("Infeasible")
	Infeasible{@Override public String toString() {return "Infeasible";}},
	@JsonProperty("Unbounded")
	Unbounded{@Override public String toString() {return "Unbounded";}},
	@JsonProperty("InfeasibleOrUnbounded")
	InfeasibleOrUnbounded{@Override public String toString() {return "InfeasibleOrUnbounded";}}
}
