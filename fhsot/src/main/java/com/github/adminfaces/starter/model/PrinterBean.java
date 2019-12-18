package com.github.adminfaces.starter.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class PrinterBean implements Serializable, Comparable<PrinterBean>{

	private Long id;
	private String brand;
	private Integer capacity;
	private BigDecimal fixedCost;

	public PrinterBean() {
		super();
	}

	public PrinterBean(Long id, String brand, Integer capacity, BigDecimal fixedCost) {
		super();
		this.id = id;
		this.brand = brand;
		this.capacity = capacity;
		this.fixedCost = fixedCost;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public BigDecimal getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(BigDecimal fixedCost) {
		this.fixedCost = fixedCost;
	}
	
	@Override
	public int compareTo(PrinterBean o) {
		return this.id.compareTo(o.getId());
	}

}