package com.fasten.wp4.database.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DistributionCenterPriority {
	@JsonProperty("Critical")
	Critical{@Override public String toString() {return "Critical";}},
	@JsonProperty("High")
	High{@Override public String toString() {return "High";}},
	@JsonProperty("Medium")
	Medium{@Override public String toString() {return "Medium";}},
	@JsonProperty("Low")
	Low{@Override public String toString() {return "Low";}},
}
