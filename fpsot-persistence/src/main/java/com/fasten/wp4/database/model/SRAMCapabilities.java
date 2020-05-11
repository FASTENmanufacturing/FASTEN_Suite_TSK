package com.fasten.wp4.database.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class SRAMCapabilities implements Serializable{

	private Double volume;
	private Double lenght;

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getLenght() {
		return lenght;
	}

	public void setLenght(Double lenght) {
		this.lenght = lenght;
	}
	
}
