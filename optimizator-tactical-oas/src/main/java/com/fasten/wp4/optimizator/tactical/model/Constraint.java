package com.fasten.wp4.optimizator.tactical.model;

public class Constraint {
	private String function;
	private Double slack;
	private Double dual;

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public Double getSlack() {
		return slack;
	}

	public void setSlack(Double slack) {
		this.slack = slack;
	}

	public Double getDual() {
		return dual;
	}

	public void setDual(Double dual) {
		this.dual = dual;
	}
}
