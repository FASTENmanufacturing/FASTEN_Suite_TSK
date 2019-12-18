package com.github.adminfaces.starter.model;

import java.io.Serializable;

public class RemoteStationBean implements Serializable, Comparable<RemoteStationBean> {

	private Long id;
	private String code;
	private String state;
	private String city;

	public RemoteStationBean() {
		super();
	}
	
	public RemoteStationBean(Long id, String code, String state, String city) {
		super();
		this.id = id;
		this.code = code;
		this.state = state;
		this.city = city;
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public int compareTo(RemoteStationBean o) {
		return this.id.compareTo(o.getId());
	}
}
