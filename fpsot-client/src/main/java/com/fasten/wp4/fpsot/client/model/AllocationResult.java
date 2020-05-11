/*
 * FPSOT OAS
 * Open API Specification REST for FPSOT - FASTEN Predictive Simulator-Optimizer Tool
 *
 * OpenAPI spec version: 0.0.1-SNAPSHOT
 * Contact: Fasten-wp4@lists.inesctec.pt
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fasten.wp4.fpsot.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasten.wp4.fpsot.client.model.AllocationResultOptimizationResult;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * AllocationResult
 */

public class AllocationResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("optimizationResult")
  private List<AllocationResultOptimizationResult> optimizationResult = null;

  @SerializedName("orderID")
  private String orderID = null;

  public AllocationResult optimizationResult(List<AllocationResultOptimizationResult> optimizationResult) {
    this.optimizationResult = optimizationResult;
    return this;
  }

  public AllocationResult addOptimizationResultItem(AllocationResultOptimizationResult optimizationResultItem) {
    if (this.optimizationResult == null) {
      this.optimizationResult = new ArrayList<>();
    }
    this.optimizationResult.add(optimizationResultItem);
    return this;
  }

   /**
   * Get optimizationResult
   * @return optimizationResult
  **/
  @ApiModelProperty(value = "")
  public List<AllocationResultOptimizationResult> getOptimizationResult() {
    return optimizationResult;
  }

  public void setOptimizationResult(List<AllocationResultOptimizationResult> optimizationResult) {
    this.optimizationResult = optimizationResult;
  }

  public AllocationResult orderID(String orderID) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AllocationResult allocationResult = (AllocationResult) o;
    return Objects.equals(this.optimizationResult, allocationResult.optimizationResult) &&
        Objects.equals(this.orderID, allocationResult.orderID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(optimizationResult, orderID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllocationResult {\n");
    
    sb.append("    optimizationResult: ").append(toIndentedString(optimizationResult)).append("\n");
    sb.append("    orderID: ").append(toIndentedString(orderID)).append("\n");
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

