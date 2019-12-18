/*
 * OpenRouteService
 * This is the openrouteservice API V2 documentation for ORS Core-Version `5.0.0`. Documentations for [older Core-Versions](https://github.com/GIScience/openrouteservice-docs/releases) can be rendered with the [Swagger-Editor](https://editor.swagger.io/).
 *
 * OpenAPI spec version: 5.0.0
 * Contact: info@openrouteservice.org
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package org.ors.direction.client.invoker.auth;

import org.ors.direction.client.invoker.Pair;

import java.util.Map;
import java.util.List;


public class OAuth implements Authentication {
  private String accessToken;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  @Override
  public void applyToParams(List<Pair> queryParams, Map<String, String> headerParams) {
    if (accessToken != null) {
      headerParams.put("Authorization", "Bearer " + accessToken);
    }
  }
}
