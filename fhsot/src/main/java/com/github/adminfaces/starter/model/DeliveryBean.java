package com.github.adminfaces.starter.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class DeliveryBean implements Serializable, Comparable<DeliveryBean> {

	private Long id;

	private String originCep;

	private String destinationCep;

	private Integer distance;

	private BigDecimal cost;

	private Integer time;

	private RemoteStationBean origin;

	private RemoteStationBean destination;

	public DeliveryBean() {
		super();
	}
	
	public DeliveryBean(Long id, String originCep, String destinationCep, Integer distance, BigDecimal cost,
			Integer time, RemoteStationBean origin, RemoteStationBean destination) {
		super();
		this.id = id;
		this.originCep = originCep;
		this.destinationCep = destinationCep;
		this.distance = distance;
		this.cost = cost;
		this.time = time;
		this.origin = origin;
		this.destination = destination;
	}



	public RemoteStationBean getOrigin() {
		return origin;
	}

	public void setOrigin(RemoteStationBean origin) {
		this.origin = origin;
	}

	public RemoteStationBean getDestination() {
		return destination;
	}

	public void setDestination(RemoteStationBean destination) {
		this.destination = destination;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOriginCep() {
		return originCep;
	}

	public void setOriginCep(String originCep) {
		this.originCep = originCep;
	}

	public String getDestinationCep() {
		return destinationCep;
	}

	public void setDestinationCep(String destinationCep) {
		this.destinationCep = destinationCep;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	@Override
	public int compareTo(DeliveryBean o) {
		return this.id.compareTo(o.getId());
	}

}
