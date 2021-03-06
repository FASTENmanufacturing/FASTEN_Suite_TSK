/*
 * Core API v1
 * API v1
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fasten.wp5.fastengateway.client.model;

import java.util.Objects;

import java.util.Arrays;
import com.fasten.wp5.fastengateway.client.model.DemandOrigin;
import com.fasten.wp5.fastengateway.client.model.DemandPart;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.io.Serializable;

/**
 * Demand
 */

public class Demand implements Serializable {
	private static final long serialVersionUID = 1L;

	@SerializedName("orderID")
	private String orderID = null;

	@SerializedName("origin")
	private DemandOrigin origin = null;

	@SerializedName("quantity")
	private Integer quantity = null;

	@SerializedName("part")
	private DemandPart part = null;

	@SerializedName("timestamp")
	private Long timestamp = null;

	public Demand orderID(String orderID) {
		this.orderID = orderID;
		return this;
	}

	/**
	 * Get orderID
	 * @return orderID
	 **/
	@ApiModelProperty(value = "")
	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public Demand origin(DemandOrigin origin) {
		this.origin = origin;
		return this;
	}

	/**
	 * Get origin
	 * @return origin
	 **/
	@ApiModelProperty(value = "")
	public DemandOrigin getOrigin() {
		return origin;
	}

	public void setOrigin(DemandOrigin origin) {
		this.origin = origin;
	}

	public Demand quantity(Integer quantity) {
		this.quantity = quantity;
		return this;
	}

	/**
	 * Get quantity
	 * @return quantity
	 **/
	@ApiModelProperty(value = "")
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Demand part(DemandPart part) {
		this.part = part;
		return this;
	}

	/**
	 * Get part
	 * @return part
	 **/
	@ApiModelProperty(value = "")
	public DemandPart getPart() {
		return part;
	}

	public void setPart(DemandPart part) {
		this.part = part;
	}

	public Demand timestamp(Long timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	/**
	 * Time that the request was made (UNIX Epoch time)
	 * @return timestamp
	 **/
	@ApiModelProperty(example = "1549549847974", value = "UNIX Epoch time")
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Demand demand = (Demand) o;
		return Objects.equals(this.orderID, demand.orderID) &&
				Objects.equals(this.origin, demand.origin) &&
				Objects.equals(this.quantity, demand.quantity) &&
				Objects.equals(this.part, demand.part) &&
				Objects.equals(this.timestamp, demand.timestamp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderID, origin, quantity, part, timestamp);
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Demand {\n");

		sb.append("    orderID: ").append(toIndentedString(orderID)).append("\n");
		sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
		sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
		sb.append("    part: ").append(toIndentedString(part)).append("\n");
		sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}

