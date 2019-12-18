package com.fasten.wp4.database.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PredictionModelSelection {
	@JsonProperty("Automatic")
	Automatic{@Override public String toString() {return "Automatic";}},
	@JsonProperty("Manual")
	Manual{@Override public String toString() {return "Manual";}},
}
