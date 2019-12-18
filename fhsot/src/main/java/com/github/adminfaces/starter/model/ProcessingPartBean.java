package com.github.adminfaces.starter.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProcessingPartBean implements Serializable, Comparable<ProcessingPartBean>{

	private Long id;
	private PrinterBean printerBean;
	private PartBean part;
	private BigDecimal cost;
	private Double time;

	public ProcessingPartBean() {
		super();
	}

	public ProcessingPartBean(Long id, PrinterBean printerBean, PartBean part, BigDecimal cost, Double time) {
		super();
		this.id = id;
		this.printerBean = printerBean;
		this.part = part;
		this.cost = cost;
		this.time = time;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PrinterBean getPrinterBean() {
		return printerBean;
	}

	public void setPrinterBean(PrinterBean printerBean) {
		this.printerBean = printerBean;
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

	public Double getTime() {
		return time;
	}

	public void setTime(Double time) {
		this.time = time;
	}
	
	@Override
	public int compareTo(ProcessingPartBean o) {
		return this.id.compareTo(o.getId());
	}

}
