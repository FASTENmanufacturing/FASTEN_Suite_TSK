package com.github.adminfaces.starter.model;

import java.io.Serializable;
import java.util.Date;

public class DemandBean implements Serializable, Comparable<DemandBean>{

	private Long id;
	private RemoteStationBean remoteStation;
	private Date orderDate;
	private Date deliveryDate;
	private PartBean part;
	private Integer quantity;
	private String code;

	public DemandBean() {
		super();
	}


	public DemandBean(Long id, RemoteStationBean remoteStation, Date orderDate, Date deliveryDate, PartBean part,
			Integer quantity, String code) {
		super();
		this.id = id;
		this.remoteStation = remoteStation;
		this.orderDate = orderDate;
		this.deliveryDate = deliveryDate;
		this.part = part;
		this.quantity = quantity;
		this.code = code;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RemoteStationBean getRemoteStation() {
		return remoteStation;
	}

	public void setRemoteStation(RemoteStationBean remoteStation) {
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

	public PartBean getPart() {
		return part;
	}

	public void setPart(PartBean part) {
		this.part = part;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public int compareTo(DemandBean o) {
		return this.id.compareTo(o.getId());
	}

}
