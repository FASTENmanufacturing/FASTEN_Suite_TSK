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
import com.fasten.wp4.database.client.model.Part;
import com.fasten.wp4.database.client.model.RemoteStation;
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
 * DemandProjected
 */

public class DemandProjected implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * Gets or Sets demandSubtype
   */
  @JsonAdapter(DemandSubtypeEnum.Adapter.class)
  public enum DemandSubtypeEnum {
    NAIVE("NAIVE"),
    
    SES("SES"),
    
    AR("AR"),
    
    HOLT("HOLT"),
    
    CF1("CF1"),
    
    CR("CR");

    private String value;

    DemandSubtypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static DemandSubtypeEnum fromValue(String text) {
      for (DemandSubtypeEnum b : DemandSubtypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<DemandSubtypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DemandSubtypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public DemandSubtypeEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return DemandSubtypeEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("demandSubtype")
  private DemandSubtypeEnum demandSubtype = null;

  /**
   * Gets or Sets demandType
   */
  @JsonAdapter(DemandTypeEnum.Adapter.class)
  public enum DemandTypeEnum {
    REAL("Real"),
    
    FORECAST("Forecast"),
    
    TRAIN("Train"),
    
    TEST("Test"),
    
    PROCESSED("Processed");

    private String value;

    DemandTypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static DemandTypeEnum fromValue(String text) {
      for (DemandTypeEnum b : DemandTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<DemandTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DemandTypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public DemandTypeEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return DemandTypeEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("demandType")
  private DemandTypeEnum demandType = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("part")
  private Part part = null;

  @SerializedName("projectedOrderDate")
  private Date projectedOrderDate = null;

  @SerializedName("quantity")
  private Integer quantity = null;

  @SerializedName("remoteStation")
  private RemoteStation remoteStation = null;

  public DemandProjected demandSubtype(DemandSubtypeEnum demandSubtype) {
    this.demandSubtype = demandSubtype;
    return this;
  }

   /**
   * Get demandSubtype
   * @return demandSubtype
  **/
  @ApiModelProperty(value = "")
  public DemandSubtypeEnum getDemandSubtype() {
    return demandSubtype;
  }

  public void setDemandSubtype(DemandSubtypeEnum demandSubtype) {
    this.demandSubtype = demandSubtype;
  }

  public DemandProjected demandType(DemandTypeEnum demandType) {
    this.demandType = demandType;
    return this;
  }

   /**
   * Get demandType
   * @return demandType
  **/
  @ApiModelProperty(value = "")
  public DemandTypeEnum getDemandType() {
    return demandType;
  }

  public void setDemandType(DemandTypeEnum demandType) {
    this.demandType = demandType;
  }

  public DemandProjected id(Long id) {
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

  public DemandProjected part(Part part) {
    this.part = part;
    return this;
  }

   /**
   * Get part
   * @return part
  **/
  @ApiModelProperty(value = "")
  public Part getPart() {
    return part;
  }

  public void setPart(Part part) {
    this.part = part;
  }

  public DemandProjected projectedOrderDate(Date projectedOrderDate) {
    this.projectedOrderDate = projectedOrderDate;
    return this;
  }

   /**
   * Get projectedOrderDate
   * @return projectedOrderDate
  **/
  @ApiModelProperty(value = "")
  public Date getProjectedOrderDate() {
    return projectedOrderDate;
  }

  public void setProjectedOrderDate(Date projectedOrderDate) {
    this.projectedOrderDate = projectedOrderDate;
  }

  public DemandProjected quantity(Integer quantity) {
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

  public DemandProjected remoteStation(RemoteStation remoteStation) {
    this.remoteStation = remoteStation;
    return this;
  }

   /**
   * Get remoteStation
   * @return remoteStation
  **/
  @ApiModelProperty(value = "")
  public RemoteStation getRemoteStation() {
    return remoteStation;
  }

  public void setRemoteStation(RemoteStation remoteStation) {
    this.remoteStation = remoteStation;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemandProjected demandProjected = (DemandProjected) o;
    return Objects.equals(this.demandSubtype, demandProjected.demandSubtype) &&
        Objects.equals(this.demandType, demandProjected.demandType) &&
        Objects.equals(this.id, demandProjected.id) &&
        Objects.equals(this.part, demandProjected.part) &&
        Objects.equals(this.projectedOrderDate, demandProjected.projectedOrderDate) &&
        Objects.equals(this.quantity, demandProjected.quantity) &&
        Objects.equals(this.remoteStation, demandProjected.remoteStation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(demandSubtype, demandType, id, part, projectedOrderDate, quantity, remoteStation);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemandProjected {\n");
    
    sb.append("    demandSubtype: ").append(toIndentedString(demandSubtype)).append("\n");
    sb.append("    demandType: ").append(toIndentedString(demandType)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    part: ").append(toIndentedString(part)).append("\n");
    sb.append("    projectedOrderDate: ").append(toIndentedString(projectedOrderDate)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    remoteStation: ").append(toIndentedString(remoteStation)).append("\n");
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

