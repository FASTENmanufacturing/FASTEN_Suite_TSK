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
import com.fasten.wp4.database.client.model.City;
import com.fasten.wp4.database.client.model.State;
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
 * Details about the RS - remote station. 
 */
@ApiModel(description = "Details about the RS - remote station. ")

public class RemoteStation implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("city")
  private City city = null;

  @SerializedName("code")
  private String code = null;

  @SerializedName("frete")
  private String frete = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("latitude")
  private Double latitude = null;

  @SerializedName("longitude")
  private Double longitude = null;

  @SerializedName("name")
  private String name = null;

  /**
   * Gets or Sets priority
   */
  @JsonAdapter(PriorityEnum.Adapter.class)
  public enum PriorityEnum {
    CRITICAL("Critical"),
    
    HIGH("High"),
    
    MEDIUM("Medium"),
    
    LOW("Low");

    private String value;

    PriorityEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static PriorityEnum fromValue(String text) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<PriorityEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final PriorityEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public PriorityEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return PriorityEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("priority")
  private PriorityEnum priority = null;

  @SerializedName("state")
  private State state = null;

  @SerializedName("unidade")
  private String unidade = null;

  public RemoteStation city(City city) {
    this.city = city;
    return this;
  }

   /**
   * The city (geographical location)
   * @return city
  **/
  @ApiModelProperty(value = "The city (geographical location)")
  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public RemoteStation code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Get code
   * @return code
  **/
  @ApiModelProperty(value = "")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public RemoteStation frete(String frete) {
    this.frete = frete;
    return this;
  }

   /**
   * Get frete
   * @return frete
  **/
  @ApiModelProperty(value = "")
  public String getFrete() {
    return frete;
  }

  public void setFrete(String frete) {
    this.frete = frete;
  }

  public RemoteStation id(Long id) {
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

  public RemoteStation latitude(Double latitude) {
    this.latitude = latitude;
    return this;
  }

   /**
   * Get latitude
   * @return latitude
  **/
  @ApiModelProperty(value = "")
  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public RemoteStation longitude(Double longitude) {
    this.longitude = longitude;
    return this;
  }

   /**
   * Get longitude
   * @return longitude
  **/
  @ApiModelProperty(value = "")
  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public RemoteStation name(String name) {
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

  public RemoteStation priority(PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

   /**
   * Get priority
   * @return priority
  **/
  @ApiModelProperty(value = "")
  public PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(PriorityEnum priority) {
    this.priority = priority;
  }

  public RemoteStation state(State state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @ApiModelProperty(value = "")
  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public RemoteStation unidade(String unidade) {
    this.unidade = unidade;
    return this;
  }

   /**
   * Get unidade
   * @return unidade
  **/
  @ApiModelProperty(value = "")
  public String getUnidade() {
    return unidade;
  }

  public void setUnidade(String unidade) {
    this.unidade = unidade;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemoteStation remoteStation = (RemoteStation) o;
    return Objects.equals(this.city, remoteStation.city) &&
        Objects.equals(this.code, remoteStation.code) &&
        Objects.equals(this.frete, remoteStation.frete) &&
        Objects.equals(this.id, remoteStation.id) &&
        Objects.equals(this.latitude, remoteStation.latitude) &&
        Objects.equals(this.longitude, remoteStation.longitude) &&
        Objects.equals(this.name, remoteStation.name) &&
        Objects.equals(this.priority, remoteStation.priority) &&
        Objects.equals(this.state, remoteStation.state) &&
        Objects.equals(this.unidade, remoteStation.unidade);
  }

  @Override
  public int hashCode() {
    return Objects.hash(city, code, frete, id, latitude, longitude, name, priority, state, unidade);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RemoteStation {\n");
    
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    frete: ").append(toIndentedString(frete)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    unidade: ").append(toIndentedString(unidade)).append("\n");
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

