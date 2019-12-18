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
@ApiModel(description = "Internal order costs")
public class InternalOrder implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "The remote station")
	@ManyToOne
	private RemoteStation remoteStation;

	@ApiModelProperty(notes = "The part")
	@ManyToOne
	private Part part;

	@ApiModelProperty(notes = "The cost to delivery to final client")
	private BigDecimal cost;

	public InternalOrder() {
		super();
	}
	
	public InternalOrder(Long id, RemoteStation remoteStation, Part part, BigDecimal cost) {
		super();
		this.id = id;
		this.remoteStation = remoteStation;
		this.part = part;
		this.cost = cost;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RemoteStation getRemoteStation() {
		return remoteStation;
	}

	public void setRemoteStation(RemoteStation remoteStation) {
		this.remoteStation = remoteStation;
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
	
}
