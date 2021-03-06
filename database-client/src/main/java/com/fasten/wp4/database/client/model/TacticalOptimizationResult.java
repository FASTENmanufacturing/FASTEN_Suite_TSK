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
import com.fasten.wp4.database.client.model.Route;
import com.fasten.wp4.database.client.model.SRAMsAllocated;
import com.fasten.wp4.database.client.model.TacticalOptimization;
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
 * Tactical optimization result
 */
@ApiModel(description = "Tactical optimization result")

public class TacticalOptimizationResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("costBenefit")
  private Boolean costBenefit = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("objectivePercent")
  private Double objectivePercent = null;

  @SerializedName("pPercent")
  private Double pPercent = null;

  @SerializedName("printers")
  private List<SRAMsAllocated> printers = null;

  @SerializedName("routes")
  private List<Route> routes = null;

  @SerializedName("study")
  private TacticalOptimization study = null;

  @SerializedName("unfeasible")
  private Boolean unfeasible = null;

  public TacticalOptimizationResult costBenefit(Boolean costBenefit) {
    this.costBenefit = costBenefit;
    return this;
  }

   /**
   * Get costBenefit
   * @return costBenefit
  **/
  @ApiModelProperty(value = "")
  public Boolean isCostBenefit() {
    return costBenefit;
  }

  public void setCostBenefit(Boolean costBenefit) {
    this.costBenefit = costBenefit;
  }

  public TacticalOptimizationResult id(Long id) {
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

  public TacticalOptimizationResult objectivePercent(Double objectivePercent) {
    this.objectivePercent = objectivePercent;
    return this;
  }

   /**
   * Get objectivePercent
   * @return objectivePercent
  **/
  @ApiModelProperty(value = "")
  public Double getObjectivePercent() {
    return objectivePercent;
  }

  public void setObjectivePercent(Double objectivePercent) {
    this.objectivePercent = objectivePercent;
  }

  public TacticalOptimizationResult pPercent(Double pPercent) {
    this.pPercent = pPercent;
    return this;
  }

   /**
   * Get pPercent
   * @return pPercent
  **/
  @ApiModelProperty(value = "")
  public Double getPPercent() {
    return pPercent;
  }

  public void setPPercent(Double pPercent) {
    this.pPercent = pPercent;
  }

  public TacticalOptimizationResult printers(List<SRAMsAllocated> printers) {
    this.printers = printers;
    return this;
  }

  public TacticalOptimizationResult addPrintersItem(SRAMsAllocated printersItem) {
    if (this.printers == null) {
      this.printers = new ArrayList<>();
    }
    this.printers.add(printersItem);
    return this;
  }

   /**
   * Get printers
   * @return printers
  **/
  @ApiModelProperty(value = "")
  public List<SRAMsAllocated> getPrinters() {
    return printers;
  }

  public void setPrinters(List<SRAMsAllocated> printers) {
    this.printers = printers;
  }

  public TacticalOptimizationResult routes(List<Route> routes) {
    this.routes = routes;
    return this;
  }

  public TacticalOptimizationResult addRoutesItem(Route routesItem) {
    if (this.routes == null) {
      this.routes = new ArrayList<>();
    }
    this.routes.add(routesItem);
    return this;
  }

   /**
   * Get routes
   * @return routes
  **/
  @ApiModelProperty(value = "")
  public List<Route> getRoutes() {
    return routes;
  }

  public void setRoutes(List<Route> routes) {
    this.routes = routes;
  }

  public TacticalOptimizationResult study(TacticalOptimization study) {
    this.study = study;
    return this;
  }

   /**
   * Get study
   * @return study
  **/
  @ApiModelProperty(value = "")
  public TacticalOptimization getStudy() {
    return study;
  }

  public void setStudy(TacticalOptimization study) {
    this.study = study;
  }

  public TacticalOptimizationResult unfeasible(Boolean unfeasible) {
    this.unfeasible = unfeasible;
    return this;
  }

   /**
   * Get unfeasible
   * @return unfeasible
  **/
  @ApiModelProperty(value = "")
  public Boolean isUnfeasible() {
    return unfeasible;
  }

  public void setUnfeasible(Boolean unfeasible) {
    this.unfeasible = unfeasible;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TacticalOptimizationResult tacticalOptimizationResult = (TacticalOptimizationResult) o;
    return Objects.equals(this.costBenefit, tacticalOptimizationResult.costBenefit) &&
        Objects.equals(this.id, tacticalOptimizationResult.id) &&
        Objects.equals(this.objectivePercent, tacticalOptimizationResult.objectivePercent) &&
        Objects.equals(this.pPercent, tacticalOptimizationResult.pPercent) &&
        Objects.equals(this.printers, tacticalOptimizationResult.printers) &&
        Objects.equals(this.routes, tacticalOptimizationResult.routes) &&
        Objects.equals(this.study, tacticalOptimizationResult.study) &&
        Objects.equals(this.unfeasible, tacticalOptimizationResult.unfeasible);
  }

  @Override
  public int hashCode() {
    return Objects.hash(costBenefit, id, objectivePercent, pPercent, printers, routes, study, unfeasible);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TacticalOptimizationResult {\n");
    
    sb.append("    costBenefit: ").append(toIndentedString(costBenefit)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    objectivePercent: ").append(toIndentedString(objectivePercent)).append("\n");
    sb.append("    pPercent: ").append(toIndentedString(pPercent)).append("\n");
    sb.append("    printers: ").append(toIndentedString(printers)).append("\n");
    sb.append("    routes: ").append(toIndentedString(routes)).append("\n");
    sb.append("    study: ").append(toIndentedString(study)).append("\n");
    sb.append("    unfeasible: ").append(toIndentedString(unfeasible)).append("\n");
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

