package com.fasten.wp4.iot.kafka.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasten.wp4.database.client.model.Delivery;
import com.fasten.wp4.database.client.model.Demand;
import com.fasten.wp4.database.client.model.Part;
import com.fasten.wp4.database.client.model.ProcessingPart;
import com.fasten.wp4.database.client.model.RemoteStation;
import com.fasten.wp4.database.client.model.SRAM;

public class HistoricalDemandResponse implements Serializable {

	private Date initialDate;// = new GregorianCalendar(2015, 06, 26).getTime();
	private Date endDate;// = new GregorianCalendar(2017, 06, 28).getTime();

	private List<RemoteStation> remoteStations;// Remote = city form demand list
	
	private List<Delivery> deliveries;
	private List<Demand> demands;
	
	private List<ProcessingPart> processingParts;
	private List<Part> parts;
	private List<SRAM> SRAMs;
	
	public List<RemoteStation> getRemoteStations() {
		return remoteStations;
	}

	public void setRemoteStations(List<RemoteStation> remoteStations) {
		this.remoteStations = remoteStations;
	}

	public List<Delivery> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<Delivery> deliveries) {
		this.deliveries = deliveries;
	}

	public List<Demand> getDemands() {
		return demands;
	}

	public void setDemands(List<Demand> demands) {
		this.demands = demands;
	}

	public List<ProcessingPart> getProcessingParts() {
		return processingParts;
	}

	public void setProcessingParts(List<ProcessingPart> processingParts) {
		this.processingParts = processingParts;
	}

	public List<Part> getParts() {
		return parts;
	}

	public void setParts(List<Part> parts) {
		this.parts = parts;
	}
	
	public List<SRAM> getSRAMs() {
		return SRAMs;
	}

	public void setSRAMs(List<SRAM> SRAMs) {
		this.SRAMs = SRAMs;
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
