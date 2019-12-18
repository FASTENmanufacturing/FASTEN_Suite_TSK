package com.github.adminfaces.starter.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class SupplyingPartBean implements Serializable, Comparable<SupplyingPartBean>{
	
	private Long id;
	private SupplierBean supplier;
	private PartBean part;
	private BigDecimal cost;

	public SupplyingPartBean() {
		super();
	}
	
	public SupplyingPartBean(Long id, SupplierBean supplier, PartBean part, BigDecimal cost) {
		super();
		this.id = id;
		this.supplier = supplier;
		this.part = part;
		this.cost = cost;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public SupplierBean getSupplier() {
		return supplier;
	}


	public void setSupplier(SupplierBean supplier) {
		this.supplier = supplier;
	}


	public PartBean getPart() {
		return part;
	}


	public void setPart(PartBean part) {
		this.part = part;
	}


	public BigDecimal getCost() {
		return cost;
	}


	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	@Override
	public int compareTo(SupplyingPartBean o) {
		return this.id.compareTo(o.getId());
	}
}
