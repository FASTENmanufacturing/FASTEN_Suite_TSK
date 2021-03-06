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
 * WebHook
 */

public class WebHook implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("consumerServiceName")
  private String consumerServiceName = null;

  @SerializedName("event")
  private String event = null;

  @SerializedName("id")
  private Long id = null;

  @SerializedName("url")
  private String url = null;

  public WebHook consumerServiceName(String consumerServiceName) {
    this.consumerServiceName = consumerServiceName;
    return this;
  }

   /**
   * Get consumerServiceName
   * @return consumerServiceName
  **/
  @ApiModelProperty(value = "")
  public String getConsumerServiceName() {
    return consumerServiceName;
  }

  public void setConsumerServiceName(String consumerServiceName) {
    this.consumerServiceName = consumerServiceName;
  }

  public WebHook event(String event) {
    this.event = event;
    return this;
  }

   /**
   * Get event
   * @return event
  **/
  @ApiModelProperty(value = "")
  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public WebHook id(Long id) {
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

  public WebHook url(String url) {
    this.url = url;
    return this;
  }

   /**
   * Get url
   * @return url
  **/
  @ApiModelProperty(value = "")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WebHook webHook = (WebHook) o;
    return Objects.equals(this.consumerServiceName, webHook.consumerServiceName) &&
        Objects.equals(this.event, webHook.event) &&
        Objects.equals(this.id, webHook.id) &&
        Objects.equals(this.url, webHook.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(consumerServiceName, event, id, url);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WebHook {\n");
    
    sb.append("    consumerServiceName: ").append(toIndentedString(consumerServiceName)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
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

