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
import com.fasten.wp4.predictive.client.model.DemandData;
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
 * ForecastingStudy
 */

public class ForecastingStudy implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * Gets or Sets models
   */
  @JsonAdapter(ModelsEnum.Adapter.class)
  public enum ModelsEnum {
    NAIVE("NAIVE"),
    
    SES("SES"),
    
    AR("AR"),
    
    HOLT("HOLT"),
    
    CR("CR"),
    
    AUTO("AUTO"),
    
    CF1("CF1"),
    
    ANN("ANN"),
    
    ELM("ELM");

    private String value;

    ModelsEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static ModelsEnum fromValue(String text) {
      for (ModelsEnum b : ModelsEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<ModelsEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ModelsEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public ModelsEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return ModelsEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("models")
  private List<ModelsEnum> models = new ArrayList<>();

  /**
   * The frequency of series (M&#x3D;Month,W&#x3D;Week,D&#x3D;Day and A&#x3D;Year).
   */
  @JsonAdapter(FrequencyEnum.Adapter.class)
  public enum FrequencyEnum {
    M("M"),
    
    W("W"),
    
    D("D"),
    
    A("A");

    private String value;

    FrequencyEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static FrequencyEnum fromValue(String text) {
      for (FrequencyEnum b : FrequencyEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<FrequencyEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final FrequencyEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public FrequencyEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return FrequencyEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("frequency")
  private FrequencyEnum frequency = null;

  @SerializedName("demands")
  private List<DemandData> demands = new ArrayList<>();

  @SerializedName("horizon")
  private Integer horizon = null;

  @SerializedName("part")
  private String part = null;

  @SerializedName("remoteStation")
  private String remoteStation = null;

  public ForecastingStudy models(List<ModelsEnum> models) {
    this.models = models;
    return this;
  }

  public ForecastingStudy addModelsItem(ModelsEnum modelsItem) {
    this.models.add(modelsItem);
    return this;
  }

   /**
   * Names of the forecasting methods
   * @return models
  **/
  @ApiModelProperty(example = "[\"NAIVE\",\"SES\",\"AR\",\"HOLT\",\"CR\",\"AUTO\",\"CF1\"]", required = true, value = "Names of the forecasting methods")
  public List<ModelsEnum> getModels() {
    return models;
  }

  public void setModels(List<ModelsEnum> models) {
    this.models = models;
  }

  public ForecastingStudy frequency(FrequencyEnum frequency) {
    this.frequency = frequency;
    return this;
  }

   /**
   * The frequency of series (M&#x3D;Month,W&#x3D;Week,D&#x3D;Day and A&#x3D;Year).
   * @return frequency
  **/
  @ApiModelProperty(example = "M", required = true, value = "The frequency of series (M=Month,W=Week,D=Day and A=Year).")
  public FrequencyEnum getFrequency() {
    return frequency;
  }

  public void setFrequency(FrequencyEnum frequency) {
    this.frequency = frequency;
  }

  public ForecastingStudy demands(List<DemandData> demands) {
    this.demands = demands;
    return this;
  }

  public ForecastingStudy addDemandsItem(DemandData demandsItem) {
    this.demands.add(demandsItem);
    return this;
  }

   /**
   * The historical (time-series) demand data.
   * @return demands
  **/
  @ApiModelProperty(required = true, value = "The historical (time-series) demand data.")
  public List<DemandData> getDemands() {
    return demands;
  }

  public void setDemands(List<DemandData> demands) {
    this.demands = demands;
  }

  public ForecastingStudy horizon(Integer horizon) {
    this.horizon = horizon;
    return this;
  }

   /**
   * Number of periods to forecast
   * minimum: 1
   * @return horizon
  **/
  @ApiModelProperty(example = "1", required = true, value = "Number of periods to forecast")
  public Integer getHorizon() {
    return horizon;
  }

  public void setHorizon(Integer horizon) {
    this.horizon = horizon;
  }

  public ForecastingStudy part(String part) {
    this.part = part;
    return this;
  }

   /**
   * Part name
   * @return part
  **/
  @ApiModelProperty(example = "Bico Dosador", value = "Part name")
  public String getPart() {
    return part;
  }

  public void setPart(String part) {
    this.part = part;
  }

  public ForecastingStudy remoteStation(String remoteStation) {
    this.remoteStation = remoteStation;
    return this;
  }

   /**
   * Demand location
   * @return remoteStation
  **/
  @ApiModelProperty(example = "São Paulo", value = "Demand location")
  public String getRemoteStation() {
    return remoteStation;
  }

  public void setRemoteStation(String remoteStation) {
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
    ForecastingStudy forecastingStudy = (ForecastingStudy) o;
    return Objects.equals(this.models, forecastingStudy.models) &&
        Objects.equals(this.frequency, forecastingStudy.frequency) &&
        Objects.equals(this.demands, forecastingStudy.demands) &&
        Objects.equals(this.horizon, forecastingStudy.horizon) &&
        Objects.equals(this.part, forecastingStudy.part) &&
        Objects.equals(this.remoteStation, forecastingStudy.remoteStation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(models, frequency, demands, horizon, part, remoteStation);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ForecastingStudy {\n");
    
    sb.append("    models: ").append(toIndentedString(models)).append("\n");
    sb.append("    frequency: ").append(toIndentedString(frequency)).append("\n");
    sb.append("    demands: ").append(toIndentedString(demands)).append("\n");
    sb.append("    horizon: ").append(toIndentedString(horizon)).append("\n");
    sb.append("    part: ").append(toIndentedString(part)).append("\n");
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

