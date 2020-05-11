/*
 * ngsi-v2
 * NGSI V2 API RC-2018.04
 *
 * OpenAPI spec version: v2
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fasten.wp4.orion.client.model;

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
import java.util.Date;
import java.io.Serializable;

/**
 * RegistrationResponseForwardingInformation
 */

public class RegistrationResponseForwardingInformation implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("timeSent")
  private Integer timeSent = null;

  @SerializedName("lastForwarding")
  private Date lastForwarding = null;

  @SerializedName("lastFailure")
  private Date lastFailure = null;

  @SerializedName("lastSuccess")
  private Date lastSuccess = null;

  public RegistrationResponseForwardingInformation timeSent(Integer timeSent) {
    this.timeSent = timeSent;
    return this;
  }

   /**
   * Get timeSent
   * @return timeSent
  **/
  @ApiModelProperty(value = "")
  public Integer getTimeSent() {
    return timeSent;
  }

  public void setTimeSent(Integer timeSent) {
    this.timeSent = timeSent;
  }

  public RegistrationResponseForwardingInformation lastForwarding(Date lastForwarding) {
    this.lastForwarding = lastForwarding;
    return this;
  }

   /**
   * Get lastForwarding
   * @return lastForwarding
  **/
  @ApiModelProperty(value = "")
  public Date getLastForwarding() {
    return lastForwarding;
  }

  public void setLastForwarding(Date lastForwarding) {
    this.lastForwarding = lastForwarding;
  }

  public RegistrationResponseForwardingInformation lastFailure(Date lastFailure) {
    this.lastFailure = lastFailure;
    return this;
  }

   /**
   * Get lastFailure
   * @return lastFailure
  **/
  @ApiModelProperty(value = "")
  public Date getLastFailure() {
    return lastFailure;
  }

  public void setLastFailure(Date lastFailure) {
    this.lastFailure = lastFailure;
  }

  public RegistrationResponseForwardingInformation lastSuccess(Date lastSuccess) {
    this.lastSuccess = lastSuccess;
    return this;
  }

   /**
   * Get lastSuccess
   * @return lastSuccess
  **/
  @ApiModelProperty(value = "")
  public Date getLastSuccess() {
    return lastSuccess;
  }

  public void setLastSuccess(Date lastSuccess) {
    this.lastSuccess = lastSuccess;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegistrationResponseForwardingInformation registrationResponseForwardingInformation = (RegistrationResponseForwardingInformation) o;
    return Objects.equals(this.timeSent, registrationResponseForwardingInformation.timeSent) &&
        Objects.equals(this.lastForwarding, registrationResponseForwardingInformation.lastForwarding) &&
        Objects.equals(this.lastFailure, registrationResponseForwardingInformation.lastFailure) &&
        Objects.equals(this.lastSuccess, registrationResponseForwardingInformation.lastSuccess);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeSent, lastForwarding, lastFailure, lastSuccess);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegistrationResponseForwardingInformation {\n");
    
    sb.append("    timeSent: ").append(toIndentedString(timeSent)).append("\n");
    sb.append("    lastForwarding: ").append(toIndentedString(lastForwarding)).append("\n");
    sb.append("    lastFailure: ").append(toIndentedString(lastFailure)).append("\n");
    sb.append("    lastSuccess: ").append(toIndentedString(lastSuccess)).append("\n");
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

