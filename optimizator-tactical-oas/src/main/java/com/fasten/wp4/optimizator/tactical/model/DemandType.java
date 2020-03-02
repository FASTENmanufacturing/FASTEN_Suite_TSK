package com.fasten.wp4.optimizator.tactical.model;

public enum DemandType {
	Real{@Override public String toString() {return "Real";}},
	Forecast{@Override public String toString() {return "Forecast";}},
	Train{@Override public String toString() {return "Train";}},
	Test{@Override public String toString() {return "Test";}},
	Processed{@Override public String toString() {return "Processed";}},
}
