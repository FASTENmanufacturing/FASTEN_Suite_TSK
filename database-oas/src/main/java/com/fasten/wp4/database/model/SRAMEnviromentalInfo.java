package com.fasten.wp4.database.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class SRAMEnviromentalInfo implements Serializable{

	private Double temperature;
	private Double humidity;

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getHumidity() {
		return humidity;
	}

	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}
}
