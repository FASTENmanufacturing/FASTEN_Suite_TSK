/*
 * Predictions API by FASTEN
 * Part of Predictive Prescriptive and Analytic Tool.
 *
 * OpenAPI spec version: 1.2
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fasten.wp4.predictive.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasten.wp4.predictive.client.model.ForecastingStudy;
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
 * ForecastStudyList
 */

public class ForecastStudyList implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("startDate")
  private String startDate = null;

  @SerializedName("endDate")
  private String endDate = null;

  @SerializedName("StudyList")
  private List<ForecastingStudy> studyList = null;

  public ForecastStudyList startDate(String startDate) {
    this.startDate = startDate;
    return this;
  }

   /**
   * The initial date in the format DD/MM/YYYY.
   * @return startDate
  **/
  @ApiModelProperty(example = "01/12/2019", required = true, value = "The initial date in the format DD/MM/YYYY.")
  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public ForecastStudyList endDate(String endDate) {
    this.endDate = endDate;
    return this;
  }

   /**
   * The end date in the format DD/MM/YYYY.
   * @return endDate
  **/
  @ApiModelProperty(example = "01/12/2020", required = true, value = "The end date in the format DD/MM/YYYY.")
  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public ForecastStudyList studyList(List<ForecastingStudy> studyList) {
    this.studyList = studyList;
    return this;
  }

  public ForecastStudyList addStudyListItem(ForecastingStudy studyListItem) {
    if (this.studyList == null) {
      this.studyList = new ArrayList<>();
    }
    this.studyList.add(studyListItem);
    return this;
  }

   /**
   * Get studyList
   * @return studyList
  **/
  @ApiModelProperty(value = "")
  public List<ForecastingStudy> getStudyList() {
    return studyList;
  }

  public void setStudyList(List<ForecastingStudy> studyList) {
    this.studyList = studyList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ForecastStudyList forecastStudyList = (ForecastStudyList) o;
    return Objects.equals(this.startDate, forecastStudyList.startDate) &&
        Objects.equals(this.endDate, forecastStudyList.endDate) &&
        Objects.equals(this.studyList, forecastStudyList.studyList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startDate, endDate, studyList);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ForecastStudyList {\n");
    
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    studyList: ").append(toIndentedString(studyList)).append("\n");
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

