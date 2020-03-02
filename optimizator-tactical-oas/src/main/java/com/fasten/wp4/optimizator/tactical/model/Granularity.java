package com.fasten.wp4.optimizator.tactical.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Granularity {
	@JsonProperty("Annual")
	Annual{@Override public String toString() {return "Annual";}},
	@JsonProperty("Monthly")
	Monthly{@Override public String toString() {return "Monthly";}},
//	@JsonProperty("Biweekly")
//	Biweekly{@Override public String toString() {return "Biweekly";}},
	@JsonProperty("Weekly")
	Weekly{@Override public String toString() {return "Weekly";}},
	@JsonProperty("Daily")
	Daily{@Override public String toString() {return "Daily";}},
}
  
