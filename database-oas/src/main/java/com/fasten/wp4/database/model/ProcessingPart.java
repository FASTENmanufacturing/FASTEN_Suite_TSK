package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.math.BigDecimal;

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

	@ApiModelProperty(notes = "The avarage time to setup the SRAM (seconds)")
	private Double avgSetupTime;

	@ApiModelProperty(notes = "The avarage time to produce the part (seconds)")
	private Double avgProducingTime;
	
	@ApiModelProperty(notes = "The standard deviation time to setup the SRAM (seconds)")
	private Double stdSetupTime;

	@ApiModelProperty(notes = "The standard deviation time to produce the part (seconds)")
	private Double stdProducingTime;
	

	public ProcessingPart() {
		super();
	}


	public ProcessingPart(Long id, SRAM SRAM, Part part, BigDecimal cost,
			Double avgSetupTime, Double avgProducingTime, Double stdSetupTime, Double stdProducingTime) {
		super();
		this.id = id;
		this.SRAM = SRAM;
		this.part = part;
		this.cost = cost;
		this.avgSetupTime = avgSetupTime;
		this.avgProducingTime = avgProducingTime;
		this.stdSetupTime = stdSetupTime;
		this.stdProducingTime = stdProducingTime;
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


	public Double getAvgSetupTime() {
		return avgSetupTime;
	}


	public void setAvgSetupTime(Double avgSetupTime) {
		this.avgSetupTime = avgSetupTime;
	}


	public Double getAvgProducingTime() {
		return avgProducingTime;
	}


	public void setAvgProducingTime(Double avgProducingTime) {
		this.avgProducingTime = avgProducingTime;
	}


	public Double getStdSetupTime() {
		return stdSetupTime;
	}


	public void setStdSetupTime(Double stdSetupTime) {
		this.stdSetupTime = stdSetupTime;
	}


	public Double getStdProducingTime() {
		return stdProducingTime;
	}


	public void setStdProducingTime(Double stdProducingTime) {
		this.stdProducingTime = stdProducingTime;
	}

	
}
