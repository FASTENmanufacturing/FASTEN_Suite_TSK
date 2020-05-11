package com.fasten.wp4.database.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SRAMProcessStatus {
	@JsonProperty("No info")
	NoInfo{@Override public String toString() {return "No info";}},
	@JsonProperty("Not ready")
	NotReady{@Override public String toString() {return "Not ready";}},
	@JsonProperty("In Operation")
	InOperation{@Override public String toString() {return "In Operation";}},
	@JsonProperty("Error")
	Error{@Override public String toString() {return "Error";}},
	@JsonProperty("Standby")
	Standby{@Override public String toString() {return "Standby";}},
}