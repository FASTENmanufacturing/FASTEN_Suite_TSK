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
import com.fasten.wp4.orion.client.model.OpqueryExpression;
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
 * SubscriptionSubjectConditions
 */

public class SubscriptionSubjectConditions implements Serializable {
  private static final long serialVersionUID = 1L;

  @SerializedName("attrs")
  private List<String> attrs = null;

  @SerializedName("expression")
  private OpqueryExpression expression = null;

  public SubscriptionSubjectConditions attrs(List<String> attrs) {
    this.attrs = attrs;
    return this;
  }

  public SubscriptionSubjectConditions addAttrsItem(String attrsItem) {
    if (this.attrs == null) {
      this.attrs = new ArrayList<>();
    }
    this.attrs.add(attrsItem);
    return this;
  }

   /**
   * Get attrs
   * @return attrs
  **/
  @ApiModelProperty(value = "")
  public List<String> getAttrs() {
    return attrs;
  }

  public void setAttrs(List<String> attrs) {
    this.attrs = attrs;
  }

  public SubscriptionSubjectConditions expression(OpqueryExpression expression) {
    this.expression = expression;
    return this;
  }

   /**
   * Get expression
   * @return expression
  **/
  @ApiModelProperty(value = "")
  public OpqueryExpression getExpression() {
    return expression;
  }

  public void setExpression(OpqueryExpression expression) {
    this.expression = expression;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubscriptionSubjectConditions subscriptionSubjectConditions = (SubscriptionSubjectConditions) o;
    return Objects.equals(this.attrs, subscriptionSubjectConditions.attrs) &&
        Objects.equals(this.expression, subscriptionSubjectConditions.expression);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attrs, expression);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubscriptionSubjectConditions {\n");
    
    sb.append("    attrs: ").append(toIndentedString(attrs)).append("\n");
    sb.append("    expression: ").append(toIndentedString(expression)).append("\n");
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
