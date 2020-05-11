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
 * Details about spare parts. 
 */
@ApiModel(description = "Details about spare parts. ")

public class Part implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("code")
  private String code = null;

  @SerializedName("depth")
  private Double depth = null;

  @SerializedName("height")
  private Double height = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("layout")
  private String layout = null;

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

  @SerializedName("width")
  private Double width = null;

  public Part code(String code) {
    this.code = code;
    return this;
  }

   /**
   * The code (SKU or another one) of the part.
   * @return code
  **/
  @ApiModelProperty(value = "The code (SKU or another one) of the part.")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Part depth(Double depth) {
    this.depth = depth;
    return this;
  }

   /**
   * Get depth
   * @return depth
  **/
  @ApiModelProperty(value = "")
  public Double getDepth() {
    return depth;
  }

  public void setDepth(Double depth) {
    this.depth = depth;
  }

  public Part height(Double height) {
    this.height = height;
    return this;
  }

   /**
   * Get height
   * @return height
  **/
  @ApiModelProperty(value = "")
  public Double getHeight() {
    return height;
  }

  public void setHeight(Double height) {
    this.height = height;
  }

  public Part id(Long id) {
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

  public Part layout(String layout) {
    this.layout = layout;
    return this;
  }

   /**
   * Get layout
   * @return layout
  **/
  @ApiModelProperty(value = "")
  public String getLayout() {
    return layout;
  }

  public void setLayout(String layout) {
    this.layout = layout;
  }

  public Part name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The part name.
   * @return name
  **/
  @ApiModelProperty(value = "The part name.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Part priority(PriorityEnum priority) {
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

  public Part width(Double width) {
    this.width = width;
    return this;
  }

   /**
   * Get width
   * @return width
  **/
  @ApiModelProperty(value = "")
  public Double getWidth() {
    return width;
  }

  public void setWidth(Double width) {
    this.width = width;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Part part = (Part) o;
    return Objects.equals(this.code, part.code) &&
        Objects.equals(this.depth, part.depth) &&
        Objects.equals(this.height, part.height) &&
        Objects.equals(this.id, part.id) &&
        Objects.equals(this.layout, part.layout) &&
        Objects.equals(this.name, part.name) &&
        Objects.equals(this.priority, part.priority) &&
        Objects.equals(this.width, part.width);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, depth, height, id, layout, name, priority, width);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Part {\n");
    
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    depth: ").append(toIndentedString(depth)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    layout: ").append(toIndentedString(layout)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
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

