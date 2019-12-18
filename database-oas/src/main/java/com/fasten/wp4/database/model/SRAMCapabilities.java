package com.fasten.wp4.database.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class SRAMCapabilities implements Serializable{

	private Double volume;
	private Double materialInUse;
	private Double materialAvailable;

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getMaterialInUse() {
		return materialInUse;
	}

	public void setMaterialInUse(Double materialInUse) {
		this.materialInUse = materialInUse;
	}

	public Double getMaterialAvailable() {
		return materialAvailable;
	}

	public void setMaterialAvailable(Double materialAvailable) {
		this.materialAvailable = materialAvailable;
	}

}
