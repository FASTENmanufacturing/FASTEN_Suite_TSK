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
import com.fasten.wp4.database.client.model.DistributionCenter;
import com.fasten.wp4.database.client.model.Part;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

/**
 * The forecasting study input data
 */
@ApiModel(description = "The forecasting study input data")

public class Prediction implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("distributionCenter")
  private DistributionCenter distributionCenter = null;

  @SerializedName("endDate")
  private Date endDate = null;

  /**
   * Gets or Sets granularity
   */
  @JsonAdapter(GranularityEnum.Adapter.class)
  public enum GranularityEnum {
    ANNUAL("Annual"),
    
    MONTHLY("Monthly"),
    
    WEEKLY("Weekly"),
    
    DAILY("Daily");

    private String value;

    GranularityEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static GranularityEnum fromValue(String text) {
      for (GranularityEnum b : GranularityEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<GranularityEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final GranularityEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public GranularityEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return GranularityEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("granularity")
  private GranularityEnum granularity = null;

  @SerializedName("horizon")
  private Integer horizon = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("initialDate")
  private Date initialDate = null;

  /**
   * Gets or Sets modelSelection
   */
  @JsonAdapter(ModelSelectionEnum.Adapter.class)
  public enum ModelSelectionEnum {
    AUTOMATIC("Automatic"),
    
    MANUAL("Manual");

    private String value;

    ModelSelectionEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static ModelSelectionEnum fromValue(String text) {
      for (ModelSelectionEnum b : ModelSelectionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<ModelSelectionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final ModelSelectionEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public ModelSelectionEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return ModelSelectionEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("modelSelection")
  private ModelSelectionEnum modelSelection = null;

  /**
   * Gets or Sets models
   */
  @JsonAdapter(ModelsEnum.Adapter.class)
  public enum ModelsEnum {
    NAIVE("NAIVE"),
    
    SES("SES"),
    
    AR("AR"),
    
    HOLT("HOLT"),
    
    CF1("CF1"),
    
    CR("CR"),
    
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
  private List<ModelsEnum> models = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("part")
  private Part part = null;

  public Prediction distributionCenter(DistributionCenter distributionCenter) {
    this.distributionCenter = distributionCenter;
    return this;
  }

   /**
   * The DC
   * @return distributionCenter
  **/
  @ApiModelProperty(value = "The DC")
  public DistributionCenter getDistributionCenter() {
    return distributionCenter;
  }

  public void setDistributionCenter(DistributionCenter distributionCenter) {
    this.distributionCenter = distributionCenter;
  }

  public Prediction endDate(Date endDate) {
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

  public Prediction granularity(GranularityEnum granularity) {
    this.granularity = granularity;
    return this;
  }

   /**
   * Get granularity
   * @return granularity
  **/
  @ApiModelProperty(value = "")
  public GranularityEnum getGranularity() {
    return granularity;
  }

  public void setGranularity(GranularityEnum granularity) {
    this.granularity = granularity;
  }

  public Prediction horizon(Integer horizon) {
    this.horizon = horizon;
    return this;
  }

   /**
   * Get horizon
   * @return horizon
  **/
  @ApiModelProperty(value = "")
  public Integer getHorizon() {
    return horizon;
  }

  public void setHorizon(Integer horizon) {
    this.horizon = horizon;
  }

  public Prediction id(Long id) {
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

  public Prediction initialDate(Date initialDate) {
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

  public Prediction modelSelection(ModelSelectionEnum modelSelection) {
    this.modelSelection = modelSelection;
    return this;
  }

   /**
   * Get modelSelection
   * @return modelSelection
  **/
  @ApiModelProperty(value = "")
  public ModelSelectionEnum getModelSelection() {
    return modelSelection;
  }

  public void setModelSelection(ModelSelectionEnum modelSelection) {
    this.modelSelection = modelSelection;
  }

  public Prediction models(List<ModelsEnum> models) {
    this.models = models;
    return this;
  }

  public Prediction addModelsItem(ModelsEnum modelsItem) {
    if (this.models == null) {
      this.models = new ArrayList<>();
    }
    this.models.add(modelsItem);
    return this;
  }

   /**
   * Get models
   * @return models
  **/
  @ApiModelProperty(value = "")
  public List<ModelsEnum> getModels() {
    return models;
  }

  public void setModels(List<ModelsEnum> models) {
    this.models = models;
  }

  public Prediction name(String name) {
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

  public Prediction part(Part part) {
    this.part = part;
    return this;
  }

   /**
   * The part
   * @return part
  **/
  @ApiModelProperty(value = "The part")
  public Part getPart() {
    return part;
  }

  public void setPart(Part part) {
    this.part = part;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Prediction prediction = (Prediction) o;
    return Objects.equals(this.distributionCenter, prediction.distributionCenter) &&
        Objects.equals(this.endDate, prediction.endDate) &&
        Objects.equals(this.granularity, prediction.granularity) &&
        Objects.equals(this.horizon, prediction.horizon) &&
        Objects.equals(this.id, prediction.id) &&
        Objects.equals(this.initialDate, prediction.initialDate) &&
        Objects.equals(this.modelSelection, prediction.modelSelection) &&
        Objects.equals(this.models, prediction.models) &&
        Objects.equals(this.name, prediction.name) &&
        Objects.equals(this.part, prediction.part);
  }

  @Override
  public int hashCode() {
    return Objects.hash(distributionCenter, endDate, granularity, horizon, id, initialDate, modelSelection, models, name, part);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Prediction {\n");
    
    sb.append("    distributionCenter: ").append(toIndentedString(distributionCenter)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    granularity: ").append(toIndentedString(granularity)).append("\n");
    sb.append("    horizon: ").append(toIndentedString(horizon)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    initialDate: ").append(toIndentedString(initialDate)).append("\n");
    sb.append("    modelSelection: ").append(toIndentedString(modelSelection)).append("\n");
    sb.append("    models: ").append(toIndentedString(models)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    part: ").append(toIndentedString(part)).append("\n");
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

