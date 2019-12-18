package com.github.adminfaces.starter.model;

import java.io.Serializable;
import java.util.Calendar;

public class DeliveryInfoBean implements Serializable, Comparable<DeliveryInfoBean>{

	private Long id;
	private String origin;
	private String destination;
	private Long distance;
	private String distanceReadable;
	private Long time;
	private String timeReadable;
	private Calendar departureTime;
	private String trafficModel;

	
	public DeliveryInfoBean(String origin, String destination, Long distance, String distanceReadable, Long time,
			String timeReadable, Calendar departureTime, String trafficModel) {
		super();
		this.origin = origin;
		this.destination = destination;
		this.distance = distance;
		this.distanceReadable = distanceReadable;
		this.time = time;
		this.timeReadable = timeReadable;
		this.departureTime = departureTime;
		this.trafficModel = trafficModel;
	}

	public DeliveryInfoBean(Long id, String origin, String destination, Long distance, String distanceReadable, Long time,
			String timeReadable, Calendar departureTime, String trafficModel) {
		super();
		this.id = id;
		this.origin = origin;
		this.destination = destination;
		this.distance = distance;
		this.distanceReadable = distanceReadable;
		this.time = time;
		this.timeReadable = timeReadable;
		this.departureTime = departureTime;
		this.trafficModel = trafficModel;
	}

	public DeliveryInfoBean() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	public String getDistanceReadable() {
		return distanceReadable;
	}

	public void setDistanceReadable(String distanceReadable) {
		this.distanceReadable = distanceReadable;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getTimeReadable() {
		return timeReadable;
	}

	public void setTimeReadable(String timeReadable) {
		this.timeReadable = timeReadable;
	}

	public Calendar getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Calendar departureTime) {
		this.departureTime = departureTime;
	}

	public String getTrafficModel() {
		return trafficModel;
	}

	public void setTrafficModel(String trafficModel) {
		this.trafficModel = trafficModel;
	}
	
	@Override
	public int compareTo(DeliveryInfoBean o) {
		return this.id.compareTo(o.getId());
	}

}
