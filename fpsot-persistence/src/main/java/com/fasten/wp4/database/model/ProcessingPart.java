package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "Details about producing the part.")
public class ProcessingPart implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "The SRAM")
	@ManyToOne
	private SRAM SRAM;

	@ApiModelProperty(notes = "The part")
	@ManyToOne
	private Part part;

	@ApiModelProperty(notes = "The cost to produce this part in that SRAM (currency)")
	private BigDecimal cost;

	private Double averagePrintTime;

	private Double estimatedPrintTime;

	private Double lastPrintTime;

	public ProcessingPart() {
		super();
	}

	public ProcessingPart(Long id, com.fasten.wp4.database.model.SRAM sRAM, Part part, BigDecimal cost,
			Double averagePrintTime, Double estimatedPrintTime, Double lastPrintTime) {
		super();
		this.id = id;
		SRAM = sRAM;
		this.part = part;
		this.cost = cost;
		this.averagePrintTime = averagePrintTime;
		this.estimatedPrintTime = estimatedPrintTime;
		this.lastPrintTime = lastPrintTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public SRAM getSRAM() {
		return SRAM;
	}

	public void setSRAM(SRAM SRAM) {
		this.SRAM = SRAM;
	}

	public Double getAveragePrintTime() {
		return averagePrintTime;
	}

	public void setAveragePrintTime(Double averagePrintTime) {
		this.averagePrintTime = averagePrintTime;
	}

	public Double getEstimatedPrintTime() {
		return estimatedPrintTime;
	}

	public void setEstimatedPrintTime(Double estimatedPrintTime) {
		this.estimatedPrintTime = estimatedPrintTime;
	}

	public Double getLastPrintTime() {
		return lastPrintTime;
	}

	public void setLastPrintTime(Double lastPrintTime) {
		this.lastPrintTime = lastPrintTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessingPart other = (ProcessingPart) obj;
		return Objects.equals(id, other.id);
	}
	
}
