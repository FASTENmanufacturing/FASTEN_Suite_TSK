package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "The optimizator input data")
public class Optimizator implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "maximum lead time (in hours) to a remote station produce and deliver the spare part")
	private Double leadTimeLimit;

	@ApiModelProperty(notes = "The annual fixed cost in Monetary")
	private BigDecimal annualFixedCost;

	@ApiModelProperty(notes = "Maximum SRAMs into a Remote Station")
	@Column(name="number_of_SRAMs")
	private Integer numberOfSRAMs;

	public Optimizator() {
		super();
	}

	public Optimizator(Long id, Double leadTimeLimit, BigDecimal annualFixedCost, Integer numberOfSRAMs) {
		super();
		this.id = id;
		this.leadTimeLimit = leadTimeLimit;
		this.annualFixedCost = annualFixedCost;
		this.numberOfSRAMs = numberOfSRAMs;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLeadTimeLimit() {
		return leadTimeLimit;
	}

	public void setLeadTimeLimit(Double leadTimeLimit) {
		this.leadTimeLimit = leadTimeLimit;
	}

	public BigDecimal getAnnualFixedCost() {
		return annualFixedCost;
	}

	public void setAnnualFixedCost(BigDecimal annualFixedCost) {
		this.annualFixedCost = annualFixedCost;
	}

	public Integer getNumberOfSRAMs() {
		return numberOfSRAMs;
	}

	public void setNumberOfSRAMs(Integer numberOfSRAMs) {
		this.numberOfSRAMs = numberOfSRAMs;
	}

}
