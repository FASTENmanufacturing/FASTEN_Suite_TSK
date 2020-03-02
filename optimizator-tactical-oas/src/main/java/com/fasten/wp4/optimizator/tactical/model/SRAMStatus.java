package com.fasten.wp4.optimizator.tactical.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SRAMStatus {
	@JsonProperty("Online")
	Online{@Override public String toString() {return "Online";}},
	@JsonProperty("Offline")
	Offline{@Override public String toString() {return "Offline";}},
}
