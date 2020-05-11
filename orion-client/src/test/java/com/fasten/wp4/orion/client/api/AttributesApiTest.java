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


package com.fasten.wp4.orion.client.api;

import com.fasten.wp4.orion.client.invoker.ApiException;
import com.fasten.wp4.orion.client.model.Entity;
import com.fasten.wp4.orion.client.model.ErrorResponse;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for AttributesApi
 */
@Ignore
public class AttributesApiTest {

    private final AttributesApi api = new AttributesApi();

    
    /**
     * 
     *
     * Returns a JSON object with the attribute data of the attribute. The object follows the JSON Representation for attributes (described in \&quot;JSON Attribute Representation\&quot; section). Response: * Successful operation uses 200 OK. * Errors use a non-2xx and (optionally) an error payload. See subsection on \&quot;Error Responses\&quot; for   more details.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getAttributeDataTest() throws ApiException {
        String entityId = null;
        String attrName = null;
        String type = null;
        String metadata = null;
        Entity response = api.getAttributeData(entityId, attrName, type, metadata);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Removes an entity attribute. Response: * Successful operation uses 204 No Content * Errors use a non-2xx and (optionally) an error payload. See subsection on \&quot;Error Responses\&quot; for   more details.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void removeASingleAttributeTest() throws ApiException {
        String entityId = null;
        String attrName = null;
        String type = null;
        api.removeASingleAttribute(entityId, attrName, type);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * The request payload is an object representing the new attribute data. Previous attribute data is replaced by the one in the request. The object follows the JSON Representation for attributes (described in \&quot;JSON Attribute Representation\&quot; section). Response: * Successful operation uses 204 No Content * Errors use a non-2xx and (optionally) an error payload. See subsection on \&quot;Error Responses\&quot; for   more details.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateAttributeDataTest() throws ApiException {
        String entityId = null;
        String attrName = null;
        Object body = null;
        String type = null;
        api.updateAttributeData(entityId, attrName, body, type);

        // TODO: test validations
    }
    
}