/*
 * IoT REST OAS
 * Open API Specification REST for IoT event management
 *
 * OpenAPI spec version: 0.0.1-SNAPSHOT
 * Contact: Fasten-wp4@lists.inesctec.pt
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fasten.wp4.iot.kafka.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.Date;
import java.io.Serializable;

/**
 * HistoricalDemandRequest
 */

public class HistoricalDemandRequest implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("endDate")
  private Date endDate = null;

  @SerializedName("initialDate")
  private Date initialDate = null;

  public HistoricalDemandRequest endDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }

   /**
   * Get endDate
   * @return endDate
  **/
  @ApiModelProperty(value = "")
  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public HistoricalDemandRequest initialDate(Date initialDate) {
    this.initialDate = initialDate;
    return this;
  }

   /**
   * Get initialDate
   * @return initialDate
  **/
  @ApiModelProperty(value = "")
  public Date getInitialDate() {
    return initialDate;
  }

  public void setInitialDate(Date initialDate) {
    this.initialDate = initialDate;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HistoricalDemandRequest historicalDemandRequest = (HistoricalDemandRequest) o;
    return Objects.equals(this.endDate, historicalDemandRequest.endDate) &&
        Objects.equals(this.initialDate, historicalDemandRequest.initialDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endDate, initialDate);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoricalDemandRequest {\n");
    
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    initialDate: ").append(toIndentedString(initialDate)).append("\n");
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
