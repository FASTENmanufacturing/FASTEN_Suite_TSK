package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "Demand input data")
public class Demand implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "The RS demanded")
	@ManyToOne
	private RemoteStation remoteStation;

	@Temporal(TemporalType.DATE)
	@ApiModelProperty(notes = "Date ordered")
	private Date orderDate;

	@Temporal(TemporalType.DATE)
	@ApiModelProperty(notes = "Date of deliver")
	private Date deliveryDate;

	@ApiModelProperty(notes = "The part")
	@ManyToOne
	private Part part;

	@ApiModelProperty(notes = "The quantitiy of parts")
	private Integer quantity;
	
	private String code;

	public Demand() {
		super();
	}

	public Demand(Long id, RemoteStation remoteStation, Date orderDate, Date deliveryDate, Part part, Integer quantity,
			String code) {
		super();
		this.id = id;
		this.remoteStation = remoteStation;
		this.orderDate = orderDate;
		this.deliveryDate = deliveryDate;
		this.part = part;
		this.quantity = quantity;
		this.code = code;
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

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}