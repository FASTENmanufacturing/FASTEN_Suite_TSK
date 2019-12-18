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
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;

/**
 * The optimizator input data
 */
@ApiModel(description = "The optimizator input data")

public class TacticalOptimization implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("capitalCost")
  private BigDecimal capitalCost = null;

  @SerializedName("clustered")
  private Boolean clustered = null;

  @SerializedName("distanceWeight")
  private BigDecimal distanceWeight = null;

  @SerializedName("endDate")
  private Date endDate = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("initialDate")
  private Date initialDate = null;

  @SerializedName("leadTimeLimit")
  private BigDecimal leadTimeLimit = null;

  @SerializedName("maximumLocations")
  private Integer maximumLocations = null;

  @SerializedName("maximumSrams")
  private Integer maximumSrams = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("productionCenterCost")
  private BigDecimal productionCenterCost = null;

  @SerializedName("sramCapacity")
  private BigDecimal sramCapacity = null;

  @SerializedName("sramCost")
  private BigDecimal sramCost = null;

  /**
   * Gets or Sets status
   */
  @JsonAdapter(StatusEnum.Adapter.class)
  public enum StatusEnum {
    VALID("Valid"),
    
    INVALID("Invalid");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<StatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StatusEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public StatusEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return StatusEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("status")
  private StatusEnum status = null;

  @SerializedName("stockoutCost")
  private BigDecimal stockoutCost = null;

  @SerializedName("timeWeight")
  private BigDecimal timeWeight = null;

  /**
   * Gets or Sets type
   */
  @JsonAdapter(TypeEnum.Adapter.class)
  public enum TypeEnum {
    LEAD_TIME("Lead Time"),
    
    COST_BENEFIT("Cost Benefit");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<TypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public TypeEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return TypeEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("type")
  private TypeEnum type = null;

  @SerializedName("usePrediction")
  private Boolean usePrediction = null;

  public TacticalOptimization capitalCost(BigDecimal capitalCost) {
    this.capitalCost = capitalCost;
    return this;
  }

   /**
   * Get capitalCost
   * @return capitalCost
  **/
  @ApiModelProperty(value = "")
  public BigDecimal getCapitalCost() {
    return capitalCost;
  }

  public void setCapitalCost(BigDecimal capitalCost) {
    this.capitalCost = capitalCost;
  }

  public TacticalOptimization clustered(Boolean clustered) {
    this.clustered = clustered;
    return this;
  }

   /**
   * Get clustered
   * @return clustered
  **/
  @ApiModelProperty(value = "")
  public Boolean isClustered() {
    return clustered;
  }

  public void setClustered(Boolean clustered) {
    this.clustered = clustered;
  }

  public TacticalOptimization distanceWeight(BigDecimal distanceWeight) {
    this.distanceWeight = distanceWeight;
    return this;
  }

   /**
   * Get distanceWeight
   * @return distanceWeight
  **/
  @ApiModelProperty(value = "")
  public BigDecimal getDistanceWeight() {
    return distanceWeight;
  }

  public void setDistanceWeight(BigDecimal distanceWeight) {
    this.distanceWeight = distanceWeight;
  }

  public TacticalOptimization endDate(Date endDate) {
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

  public TacticalOptimization id(Long id) {
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

  public TacticalOptimization initialDate(Date initialDate) {
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

  public TacticalOptimization leadTimeLimit(BigDecimal leadTimeLimit) {
    this.leadTimeLimit = leadTimeLimit;
    return this;
  }

   /**
   * Get leadTimeLimit
   * @return leadTimeLimit
  **/
  @ApiModelProperty(value = "")
  public BigDecimal getLeadTimeLimit() {
    return leadTimeLimit;
  }

  public void setLeadTimeLimit(BigDecimal leadTimeLimit) {
    this.leadTimeLimit = leadTimeLimit;
  }

  public TacticalOptimization maximumLocations(Integer maximumLocations) {
    this.maximumLocations = maximumLocations;
    return this;
  }

   /**
   * Get maximumLocations
   * @return maximumLocations
  **/
  @ApiModelProperty(value = "")
  public Integer getMaximumLocations() {
    return maximumLocations;
  }

  public void setMaximumLocations(Integer maximumLocations) {
    this.maximumLocations = maximumLocations;
  }

  public TacticalOptimization maximumSrams(Integer maximumSrams) {
    this.maximumSrams = maximumSrams;
    return this;
  }

   /**
   * Get maximumSrams
   * @return maximumSrams
  **/
  @ApiModelProperty(value = "")
  public Integer getMaximumSrams() {
    return maximumSrams;
  }

  public void setMaximumSrams(Integer maximumSrams) {
    this.maximumSrams = maximumSrams;
  }

  public TacticalOptimization name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TacticalOptimization productionCenterCost(BigDecimal productionCenterCost) {
    this.productionCenterCost = productionCenterCost;
    return this;
  }

   /**
   * Get productionCenterCost
   * @return productionCenterCost
  **/
  @ApiModelProperty(value = "")
  public BigDecimal getProductionCenterCost() {
    return productionCenterCost;
  }

  public void setProductionCenterCost(BigDecimal productionCenterCost) {
    this.productionCenterCost = productionCenterCost;
  }

  public TacticalOptimization sramCapacity(BigDecimal sramCapacity) {
    this.sramCapacity = sramCapacity;
    return this;
  }

   /**
   * Get sramCapacity
   * @return sramCapacity
  **/
  @ApiModelProperty(value = "")
  public BigDecimal getSramCapacity() {
    return sramCapacity;
  }

  public void setSramCapacity(BigDecimal sramCapacity) {
    this.sramCapacity = sramCapacity;
  }

  public TacticalOptimization sramCost(BigDecimal sramCost) {
    this.sramCost = sramCost;
    return this;
  }

   /**
   * Get sramCost
   * @return sramCost
  **/
  @ApiModelProperty(value = "")
  public BigDecimal getSramCost() {
    return sramCost;
  }

  public void setSramCost(BigDecimal sramCost) {
    this.sramCost = sramCost;
  }

  public TacticalOptimization status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(value = "")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public TacticalOptimization stockoutCost(BigDecimal stockoutCost) {
    this.stockoutCost = stockoutCost;
    return this;
  }

   /**
   * Get stockoutCost
   * @return stockoutCost
  **/
  @ApiModelProperty(value = "")
  public BigDecimal getStockoutCost() {
    return stockoutCost;
  }

  public void setStockoutCost(BigDecimal stockoutCost) {
    this.stockoutCost = stockoutCost;
  }

  public TacticalOptimization timeWeight(BigDecimal timeWeight) {
    this.timeWeight = timeWeight;
    return this;
  }

   /**
   * Get timeWeight
   * @return timeWeight
  **/
  @ApiModelProperty(value = "")
  public BigDecimal getTimeWeight() {
    return timeWeight;
  }

  public void setTimeWeight(BigDecimal timeWeight) {
    this.timeWeight = timeWeight;
  }

  public TacticalOptimization type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public TacticalOptimization usePrediction(Boolean usePrediction) {
    this.usePrediction = usePrediction;
    return this;
  }

   /**
   * Get usePrediction
   * @return usePrediction
  **/
  @ApiModelProperty(value = "")
  public Boolean isUsePrediction() {
    return usePrediction;
  }

  public void setUsePrediction(Boolean usePrediction) {
    this.usePrediction = usePrediction;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TacticalOptimization tacticalOptimization = (TacticalOptimization) o;
    return Objects.equals(this.capitalCost, tacticalOptimization.capitalCost) &&
        Objects.equals(this.clustered, tacticalOptimization.clustered) &&
        Objects.equals(this.distanceWeight, tacticalOptimization.distanceWeight) &&
        Objects.equals(this.endDate, tacticalOptimization.endDate) &&
        Objects.equals(this.id, tacticalOptimization.id) &&
        Objects.equals(this.initialDate, tacticalOptimization.initialDate) &&
        Objects.equals(this.leadTimeLimit, tacticalOptimization.leadTimeLimit) &&
        Objects.equals(this.maximumLocations, tacticalOptimization.maximumLocations) &&
        Objects.equals(this.maximumSrams, tacticalOptimization.maximumSrams) &&
        Objects.equals(this.name, tacticalOptimization.name) &&
        Objects.equals(this.productionCenterCost, tacticalOptimization.productionCenterCost) &&
        Objects.equals(this.sramCapacity, tacticalOptimization.sramCapacity) &&
        Objects.equals(this.sramCost, tacticalOptimization.sramCost) &&
        Objects.equals(this.status, tacticalOptimization.status) &&
        Objects.equals(this.stockoutCost, tacticalOptimization.stockoutCost) &&
        Objects.equals(this.timeWeight, tacticalOptimization.timeWeight) &&
        Objects.equals(this.type, tacticalOptimization.type) &&
        Objects.equals(this.usePrediction, tacticalOptimization.usePrediction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(capitalCost, clustered, distanceWeight, endDate, id, initialDate, leadTimeLimit, maximumLocations, maximumSrams, name, productionCenterCost, sramCapacity, sramCost, status, stockoutCost, timeWeight, type, usePrediction);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TacticalOptimization {\n");
    
    sb.append("    capitalCost: ").append(toIndentedString(capitalCost)).append("\n");
    sb.append("    clustered: ").append(toIndentedString(clustered)).append("\n");
    sb.append("    distanceWeight: ").append(toIndentedString(distanceWeight)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    initialDate: ").append(toIndentedString(initialDate)).append("\n");
    sb.append("    leadTimeLimit: ").append(toIndentedString(leadTimeLimit)).append("\n");
    sb.append("    maximumLocations: ").append(toIndentedString(maximumLocations)).append("\n");
    sb.append("    maximumSrams: ").append(toIndentedString(maximumSrams)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    productionCenterCost: ").append(toIndentedString(productionCenterCost)).append("\n");
    sb.append("    sramCapacity: ").append(toIndentedString(sramCapacity)).append("\n");
    sb.append("    sramCost: ").append(toIndentedString(sramCost)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    stockoutCost: ").append(toIndentedString(stockoutCost)).append("\n");
    sb.append("    timeWeight: ").append(toIndentedString(timeWeight)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    usePrediction: ").append(toIndentedString(usePrediction)).append("\n");
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

