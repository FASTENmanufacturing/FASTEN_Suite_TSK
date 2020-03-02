package com.fasten.wp4.optimizator.tactical.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "Demand input data")
public class Delivery implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String originCep;

	private String destinationCep;

	@ApiModelProperty(notes = "Travel distance (meters)")
	private Integer distance;

	@ApiModelProperty(notes = "The cost to delivery (Monetary)")
	private BigDecimal cost;

	@ApiModelProperty(notes = "Travel time (seconds)")
	private Integer time;

	@ManyToOne
	private RemoteStation origin;

	@ManyToOne
	private RemoteStation destination;

	public Delivery() {
		super();
	}

	public Delivery(Long id, String originCep, String destinationCep, Integer distance, BigDecimal cost, Integer time,
			RemoteStation origin, RemoteStation destination) {
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

	public RemoteStation getOrigin() {
		return origin;
	}

	public void setOrigin(RemoteStation origin) {
		this.origin = origin;
	}

	public RemoteStation getDestination() {
		return destination;
	}

	public void setDestination(RemoteStation destination) {
		this.destination = destination;
	}

}
