package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "Details about SRAM. ")
public class Stock implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "Percentage of the average stock applied to calculate capital costs.")
	private Double capitalCost;

	@ApiModelProperty(notes = "The cost of having a stockout of a part on a remote station during a review time")
	private BigDecimal stockoutCost;

	public Stock() {
		super();
	}

	public Stock(Long id, Double capitalCost, BigDecimal stockoutCost) {
		super();
		this.id = id;
		this.capitalCost = capitalCost;
		this.stockoutCost = stockoutCost;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getCapitalCost() {
		return capitalCost;
	}

	public void setCapitalCost(Double capitalCost) {
		this.capitalCost = capitalCost;
	}

	public BigDecimal getStockoutCost() {
		return stockoutCost;
	}

	public void setStockoutCost(BigDecimal stockoutCost) {
		this.stockoutCost = stockoutCost;
	}

}
