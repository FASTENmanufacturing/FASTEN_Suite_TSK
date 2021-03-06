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


package org.ors.geocode.client.api;

import org.ors.geocode.client.invoker.ApiException;
import org.ors.geocode.client.model.AddressResponse;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for GeocodeApi
 */
@Ignore
public class GeocodeApiTest {

    private final GeocodeApi api = new GeocodeApi();

    
    /**
     * Forward Geocode Service
     *
     * Returns a JSON formatted list of objects corresponding to the search input. &#x60;boundary.*&#x60;-parameters can be combined if they are overlapping. **The interactivity for this enpoint is experimental!** [Please refer to this external Documentation](https://github.com/pelias/documentation/blob/master/search.md#search-the-world) 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void geocodeSearchGetTest() throws ApiException {
        String apiKey = null;
        String text = null;
        Float focusPointLon = null;
        Float focusPointLat = null;
        Float boundaryRectMinLon = null;
        Float boundaryRectMinLat = null;
        Float boundaryRectMaxLon = null;
        Float boundaryRectMaxLat = null;
        Float boundaryCircleLon = null;
        Float boundaryCircleLat = null;
        Float boundaryCircleRadius = null;
        String boundaryCountry = null;
        List<String> sources = null;
        List<String> layers = null;
        Integer size = null;
        AddressResponse response = api.geocodeSearchGet(apiKey, text, focusPointLon, focusPointLat, boundaryRectMinLon, boundaryRectMinLat, boundaryRectMaxLon, boundaryRectMaxLat, boundaryCircleLon, boundaryCircleLat, boundaryCircleRadius, boundaryCountry, sources, layers, size);

        // TODO: test validations
    }
    
}
