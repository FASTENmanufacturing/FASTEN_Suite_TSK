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


package org.ors.geocode.client.invoker.auth;

import org.ors.geocode.client.invoker.Pair;

import java.util.Map;
import java.util.List;

public interface Authentication {
    /**
     * Apply authentication settings to header and query params.
     *
     * @param queryParams List of query parameters
     * @param headerParams Map of header parameters
     */
    void applyToParams(List<Pair> queryParams, Map<String, String> headerParams);
}
