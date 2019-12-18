package com.github.adminfaces.starter.model;

import java.io.Serializable;
import java.util.Calendar;

public class ProcessingPartInfoBean implements Serializable, Comparable<ProcessingPartInfoBean>{

	private Long id;
	private PartBean part;
	private String local;
	private Calendar startPrinting;
	private PrinterBean printer;
	private Double setupTime;
	private Double warmingTime;
	private Double preparationTime;
	private Double printingTime;
	private Double coolingTime;
	private Double totalCoolingTime;
	private Double totalPrintingTime;

	public ProcessingPartInfoBean(Long id, PartBean part, String local, Calendar start, PrinterBean printer, Double stupTime,
			Double warmingTime, Double preparationTime, Double printingTime, Double coolingTime,
			Double totalCoolingTime, Double totalPrintingTime) {
		super();
		this.id = id;
		this.part = part;
		this.local = local;
		this.startPrinting = start;
		this.printer = printer;
		this.setupTime = stupTime;
		this.warmingTime = warmingTime;
		this.preparationTime = preparationTime;
		this.printingTime = printingTime;
		this.coolingTime = coolingTime;
		this.totalCoolingTime = totalCoolingTime;
		this.totalPrintingTime = totalPrintingTime;
	}

	public ProcessingPartInfoBean(PartBean part, String local, Calendar start, PrinterBean printer, Double stupTime,
			Double warmingTime, Double preparationTime, Double printingTime, Double coolingTime,
			Double totalCoolingTime, Double totalPrintingTime) {
		super();
		this.part = part;
		this.local = local;
		this.startPrinting = start;
		this.printer = printer;
		this.setupTime = stupTime;
		this.warmingTime = warmingTime;
		this.preparationTime = preparationTime;
		this.printingTime = printingTime;
		this.coolingTime = coolingTime;
		this.totalCoolingTime = totalCoolingTime;
		this.totalPrintingTime = totalPrintingTime;
	}

	public ProcessingPartInfoBean() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PartBean getPart() {
		return part;
	}

	public void setPart(PartBean part) {
		this.part = part;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	

	public Calendar getStartPrinting() {
		return startPrinting;
	}

	public void setStartPrinting(Calendar startPrinting) {
		this.startPrinting = startPrinting;
	}

	public Double getSetupTime() {
		return setupTime;
	}

	public void setSetupTime(Double setupTime) {
		this.setupTime = setupTime;
	}

	public PrinterBean getPrinter() {
		return printer;
	}

	public void setPrinter(PrinterBean printer) {
		this.printer = printer;
	}

	public Double getWarmingTime() {
		return warmingTime;
	}

	public void setWarmingTime(Double warmingTime) {
		this.warmingTime = warmingTime;
	}

	public Double getPreparationTime() {
		return preparationTime;
	}

	public void setPreparationTime(Double preparationTime) {
		this.preparationTime = preparationTime;
	}

	public Double getPrintingTime() {
		return printingTime;
	}

	public void setPrintingTime(Double printingTime) {
		this.printingTime = printingTime;
	}

	public Double getCoolingTime() {
		return coolingTime;
	}

	public void setCoolingTime(Double coolingTime) {
		this.coolingTime = coolingTime;
	}

	public Double getTotalCoolingTime() {
		return totalCoolingTime;
	}

	public void setTotalCoolingTime(Double totalCoolingTime) {
		this.totalCoolingTime = totalCoolingTime;
	}

	public Double getTotalPrintingTime() {
		return totalPrintingTime;
	}

	public void setTotalPrintingTime(Double totalPrintingTime) {
		this.totalPrintingTime = totalPrintingTime;
	}
	
	@Override
	public int compareTo(ProcessingPartInfoBean o) {
		return this.id.compareTo(o.getId());
	}

}
