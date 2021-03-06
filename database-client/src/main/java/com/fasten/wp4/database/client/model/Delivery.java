/*
 * Database OAS
 * Open API Specification REST for FASTEN Holistic Simulator-Optimizer Tool
 *
 * OpenAPI spec version: 0.0.1-SNAPSHOT
 * Contact: Fasten-wp4@lists.inesctec.pt
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fasten.wp4.database.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasten.wp4.database.client.model.DistributionCenter;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.math.BigDecimal;
import java.io.Serializable;

/**
 * Demand input data
 */
@ApiModel(description = "Demand input data")

public class Delivery implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("cost")
  private BigDecimal cost = null;

  @SerializedName("destination")
  private DistributionCenter destination = null;

  @SerializedName("destinationCep")
  private String destinationCep = null;

  @SerializedName("distance")
  private Integer distance = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("origin")
  private DistributionCenter origin = null;

  @SerializedName("originCep")
  private String originCep = null;

  @SerializedName("time")
  private Integer time = null;

  public Delivery cost(BigDecimal cost) {
    this.cost = cost;
    return this;
  }

   /**
   * The cost to delivery (Monetary)
   * @return cost
  **/
  @ApiModelProperty(value = "The cost to delivery (Monetary)")
  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public Delivery destination(DistributionCenter destination) {
    this.destination = destination;
    return this;
  }

   /**
   * Get destination
   * @return destination
  **/
  @ApiModelProperty(value = "")
  public DistributionCenter getDestination() {
    return destination;
  }

  public void setDestination(DistributionCenter destination) {
    this.destination = destination;
  }

  public Delivery destinationCep(String destinationCep) {
    this.destinationCep = destinationCep;
    return this;
  }

   /**
   * Get destinationCep
   * @return destinationCep
  **/
  @ApiModelProperty(value = "")
  public String getDestinationCep() {
    return destinationCep;
  }

  public void setDestinationCep(String destinationCep) {
    this.destinationCep = destinationCep;
  }

  public Delivery distance(Integer distance) {
    this.distance = distance;
    return this;
  }

   /**
   * Travel distance (meters)
   * @return distance
  **/
  @ApiModelProperty(value = "Travel distance (meters)")
  public Integer getDistance() {
    return distance;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  public Delivery id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(value = "")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Delivery origin(DistributionCenter origin) {
    this.origin = origin;
    return this;
  }

   /**
   * Get origin
   * @return origin
  **/
  @ApiModelProperty(value = "")
  public DistributionCenter getOrigin() {
    return origin;
  }

  public void setOrigin(DistributionCenter origin) {
    this.origin = origin;
  }

  public Delivery originCep(String originCep) {
    this.originCep = originCep;
    return this;
  }

   /**
   * Get originCep
   * @return originCep
  **/
  @ApiModelProperty(value = "")
  public String getOriginCep() {
    return originCep;
  }

  public void setOriginCep(String originCep) {
    this.originCep = originCep;
  }

  public Delivery time(Integer time) {
    this.time = time;
    return this;
  }

   /**
   * Travel time (seconds)
   * @return time
  **/
  @ApiModelProperty(value = "Travel time (seconds)")
  public Integer getTime() {
    return time;
  }

  public void setTime(Integer time) {
    this.time = time;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Delivery delivery = (Delivery) o;
    return Objects.equals(this.cost, delivery.cost) &&
        Objects.equals(this.destination, delivery.destination) &&
        Objects.equals(this.destinationCep, delivery.destinationCep) &&
        Objects.equals(this.distance, delivery.distance) &&
        Objects.equals(this.id, delivery.id) &&
        Objects.equals(this.origin, delivery.origin) &&
        Objects.equals(this.originCep, delivery.originCep) &&
        Objects.equals(this.time, delivery.time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cost, destination, destinationCep, distance, id, origin, originCep, time);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Delivery {\n");
    
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("    destination: ").append(toIndentedString(destination)).append("\n");
    sb.append("    destinationCep: ").append(toIndentedString(destinationCep)).append("\n");
    sb.append("    distance: ").append(toIndentedString(distance)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    origin: ").append(toIndentedString(origin)).append("\n");
    sb.append("    originCep: ").append(toIndentedString(originCep)).append("\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
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

