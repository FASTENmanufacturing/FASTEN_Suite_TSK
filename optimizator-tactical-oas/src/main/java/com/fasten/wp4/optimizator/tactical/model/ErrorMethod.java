package com.fasten.wp4.optimizator.tactical.model;

public enum ErrorMethod {
	RMSE{@Override public String toString() {return "RMSE";}},
	MAPE{@Override public String toString() {return "MAPE";}},
	MASE{@Override public String toString() {return "MASE";}}
}
