/*
 * Probability Distribution Generator
 * API Description
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fasten.wp4.probabilitydistribution.client.model;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * ProbabilityDistributionResultParameters
 */

public class ProbabilityDistributionResultParameters implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("meanlog")
  private List<BigDecimal> meanlog = null;

  @SerializedName("sdlog")
  private List<BigDecimal> sdlog = null;

  public ProbabilityDistributionResultParameters meanlog(List<BigDecimal> meanlog) {
    this.meanlog = meanlog;
    return this;
  }

  public ProbabilityDistributionResultParameters addMeanlogItem(BigDecimal meanlogItem) {
    if (this.meanlog == null) {
      this.meanlog = new ArrayList<>();
    }
    this.meanlog.add(meanlogItem);
    return this;
  }

   /**
   * Get meanlog
   * @return meanlog
  **/
  @ApiModelProperty(value = "")
  public List<BigDecimal> getMeanlog() {
    return meanlog;
  }

  public void setMeanlog(List<BigDecimal> meanlog) {
    this.meanlog = meanlog;
  }

  public ProbabilityDistributionResultParameters sdlog(List<BigDecimal> sdlog) {
    this.sdlog = sdlog;
    return this;
  }

  public ProbabilityDistributionResultParameters addSdlogItem(BigDecimal sdlogItem) {
    if (this.sdlog == null) {
      this.sdlog = new ArrayList<>();
    }
    this.sdlog.add(sdlogItem);
    return this;
  }

   /**
   * Get sdlog
   * @return sdlog
  **/
  @ApiModelProperty(value = "")
  public List<BigDecimal> getSdlog() {
    return sdlog;
  }

  public void setSdlog(List<BigDecimal> sdlog) {
    this.sdlog = sdlog;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProbabilityDistributionResultParameters probabilityDistributionResultParameters = (ProbabilityDistributionResultParameters) o;
    return Objects.equals(this.meanlog, probabilityDistributionResultParameters.meanlog) &&
        Objects.equals(this.sdlog, probabilityDistributionResultParameters.sdlog);
  }

  @Override
  public int hashCode() {
    return Objects.hash(meanlog, sdlog);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProbabilityDistributionResultParameters {\n");
    
    sb.append("    meanlog: ").append(toIndentedString(meanlog)).append("\n");
    sb.append("    sdlog: ").append(toIndentedString(sdlog)).append("\n");
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

