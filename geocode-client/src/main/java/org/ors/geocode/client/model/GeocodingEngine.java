/*
 * Openrouteservice
 * This is the openrouteservice API V1 documentation for ORS Core-Version `5.0.0`
 *
 * OpenAPI spec version: 5.0.0
 * Contact: support@openrouteservice.org
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package org.ors.geocode.client.model;

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
 * GeocodingEngine
 */

public class GeocodingEngine implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("name")
  private String name = null;

  @SerializedName("author")
  private String author = null;

  @SerializedName("version")
  private String version = null;

  public GeocodingEngine name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Source engine, Pelias
   * @return name
  **/
  @ApiModelProperty(value = "Source engine, Pelias")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GeocodingEngine author(String author) {
    this.author = author;
    return this;
  }

   /**
   * Main author, Mapzen
   * @return author
  **/
  @ApiModelProperty(value = "Main author, Mapzen")
  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public GeocodingEngine version(String version) {
    this.version = version;
    return this;
  }

   /**
   * Engine version
   * @return version
  **/
  @ApiModelProperty(value = "Engine version")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GeocodingEngine geocodingEngine = (GeocodingEngine) o;
    return Objects.equals(this.name, geocodingEngine.name) &&
        Objects.equals(this.author, geocodingEngine.author) &&
        Objects.equals(this.version, geocodingEngine.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, author, version);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeocodingEngine {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

