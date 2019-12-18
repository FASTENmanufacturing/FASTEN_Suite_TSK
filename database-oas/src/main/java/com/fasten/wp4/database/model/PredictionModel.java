package com.fasten.wp4.database.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PredictionModel {
	@JsonProperty("NAIVE")
	NAIVE{@Override public String toString() {return "NAIVE";}},
	@JsonProperty("SES")
	SES{@Override public String toString() {return "SES";}},
	@JsonProperty("AR")
	AR{@Override public String toString() {return "AR";}},
	@JsonProperty("HOLT")
	HOLT{@Override public String toString() {return "HOLT";}},
	@JsonProperty("CF1")
	CF1{@Override public String toString() {return "CF1";}},
	@JsonProperty("CR")
	CR{@Override public String toString() {return "CR";}}
}
