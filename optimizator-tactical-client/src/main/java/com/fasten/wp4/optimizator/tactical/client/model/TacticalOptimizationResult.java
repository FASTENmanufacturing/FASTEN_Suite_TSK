/*
 * Tactical Optimizator OAS
 * Tactical Optimizator Open API Specification REST for FASTEN Holistic Simulator-Optimizer Tool
 *
 * OpenAPI spec version: 0.0.1-SNAPSHOT
 * Contact: Fasten-wp4@lists.inesctec.pt
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fasten.wp4.optimizator.tactical.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasten.wp4.optimizator.tactical.client.model.InternalSupply;
import com.fasten.wp4.optimizator.tactical.client.model.Production;
import com.fasten.wp4.optimizator.tactical.client.model.Route;
import com.fasten.wp4.optimizator.tactical.client.model.SRAMsAllocated;
import com.fasten.wp4.optimizator.tactical.client.model.TacticalOptimization;
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

  @SerializedName("id")
  private Long id = null;

  @SerializedName("internalSuppliers")
  private List<InternalSupply> internalSuppliers = null;

  @SerializedName("printers")
  private List<SRAMsAllocated> printers = null;

  @SerializedName("productions")
  private List<Production> productions = null;

  @SerializedName("routes")
  private List<Route> routes = null;

  @SerializedName("study")
  private TacticalOptimization study = null;

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

  public TacticalOptimizationResult internalSuppliers(List<InternalSupply> internalSuppliers) {
    this.internalSuppliers = internalSuppliers;
    return this;
  }

  public TacticalOptimizationResult addInternalSuppliersItem(InternalSupply internalSuppliersItem) {
    if (this.internalSuppliers == null) {
      this.internalSuppliers = new ArrayList<>();
    }
    this.internalSuppliers.add(internalSuppliersItem);
    return this;
  }

   /**
   * Get internalSuppliers
   * @return internalSuppliers
  **/
  @ApiModelProperty(value = "")
  public List<InternalSupply> getInternalSuppliers() {
    return internalSuppliers;
  }

  public void setInternalSuppliers(List<InternalSupply> internalSuppliers) {
    this.internalSuppliers = internalSuppliers;
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

  public TacticalOptimizationResult productions(List<Production> productions) {
    this.productions = productions;
    return this;
  }

  public TacticalOptimizationResult addProductionsItem(Production productionsItem) {
    if (this.productions == null) {
      this.productions = new ArrayList<>();
    }
    this.productions.add(productionsItem);
    return this;
  }

   /**
   * Get productions
   * @return productions
  **/
  @ApiModelProperty(value = "")
  public List<Production> getProductions() {
    return productions;
  }

  public void setProductions(List<Production> productions) {
    this.productions = productions;
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TacticalOptimizationResult tacticalOptimizationResult = (TacticalOptimizationResult) o;
    return Objects.equals(this.id, tacticalOptimizationResult.id) &&
        Objects.equals(this.internalSuppliers, tacticalOptimizationResult.internalSuppliers) &&
        Objects.equals(this.printers, tacticalOptimizationResult.printers) &&
        Objects.equals(this.productions, tacticalOptimizationResult.productions) &&
        Objects.equals(this.routes, tacticalOptimizationResult.routes) &&
        Objects.equals(this.study, tacticalOptimizationResult.study);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, internalSuppliers, printers, productions, routes, study);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TacticalOptimizationResult {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    internalSuppliers: ").append(toIndentedString(internalSuppliers)).append("\n");
    sb.append("    printers: ").append(toIndentedString(printers)).append("\n");
    sb.append("    productions: ").append(toIndentedString(productions)).append("\n");
    sb.append("    routes: ").append(toIndentedString(routes)).append("\n");
    sb.append("    study: ").append(toIndentedString(study)).append("\n");
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

