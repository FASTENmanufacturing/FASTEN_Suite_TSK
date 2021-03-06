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
 * Sort
 */

public class Sort implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("empty")
  private Boolean empty = null;

  @SerializedName("sorted")
  private Boolean sorted = null;

  @SerializedName("unsorted")
  private Boolean unsorted = null;

  public Sort empty(Boolean empty) {
    this.empty = empty;
    return this;
  }

   /**
   * Get empty
   * @return empty
  **/
  @ApiModelProperty(value = "")
  public Boolean isEmpty() {
    return empty;
  }

  public void setEmpty(Boolean empty) {
    this.empty = empty;
  }

  public Sort sorted(Boolean sorted) {
    this.sorted = sorted;
    return this;
  }

   /**
   * Get sorted
   * @return sorted
  **/
  @ApiModelProperty(value = "")
  public Boolean isSorted() {
    return sorted;
  }

  public void setSorted(Boolean sorted) {
    this.sorted = sorted;
  }

  public Sort unsorted(Boolean unsorted) {
    this.unsorted = unsorted;
    return this;
  }

   /**
   * Get unsorted
   * @return unsorted
  **/
  @ApiModelProperty(value = "")
  public Boolean isUnsorted() {
    return unsorted;
  }

  public void setUnsorted(Boolean unsorted) {
    this.unsorted = unsorted;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sort sort = (Sort) o;
    return Objects.equals(this.empty, sort.empty) &&
        Objects.equals(this.sorted, sort.sorted) &&
        Objects.equals(this.unsorted, sort.unsorted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(empty, sorted, unsorted);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Sort {\n");
    
    sb.append("    empty: ").append(toIndentedString(empty)).append("\n");
    sb.append("    sorted: ").append(toIndentedString(sorted)).append("\n");
    sb.append("    unsorted: ").append(toIndentedString(unsorted)).append("\n");
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

