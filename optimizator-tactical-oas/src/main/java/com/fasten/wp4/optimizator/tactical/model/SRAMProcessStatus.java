package com.fasten.wp4.optimizator.tactical.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SRAMProcessStatus {
	@JsonProperty("Setup")
	Setup{@Override public String toString() {return "Setup";}},
	@JsonProperty("In Operation")
	InOperation{@Override public String toString() {return "In Operation";}},
	@JsonProperty("Error")
	Error{@Override public String toString() {return "Error";}},
	@JsonProperty("Done")
	Done{@Override public String toString() {return "Done";}},
}