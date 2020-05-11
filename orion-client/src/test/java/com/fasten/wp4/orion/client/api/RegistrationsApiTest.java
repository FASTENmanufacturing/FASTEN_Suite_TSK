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
import com.fasten.wp4.orion.client.model.ErrorResponse;
import com.fasten.wp4.orion.client.model.Registration;
import com.fasten.wp4.orion.client.model.RegistrationResponse;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for RegistrationsApi
 */
@Ignore
public class RegistrationsApiTest {

    private final RegistrationsApi api = new RegistrationsApi();

    
    /**
     * 
     *
     * Creates a new context provider registration. This is typically used for binding context sources as providers of certain data. The registration is represented by a JSON object as described at the beginning of this section. Response: * Successful operation uses 201 Created * Errors use a non-2xx and (optionally) an error payload. See subsection on \&quot;Error Responses\&quot; for more details.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createRegistrationsTest() throws ApiException {
        Registration body = null;
        api.createRegistrations(body);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Cancels a context provider registration. Response: * Successful operation uses 204 No Content * Errors use a non-2xx and (optionally) an error payload. See subsection on \&quot;Error Responses\&quot; for more details.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteRegistrationTest() throws ApiException {
        String registrationId = null;
        api.deleteRegistration(registrationId);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * The response is the registration represented by a JSON object as described at the beginning of this section. Response: * Successful operation uses 200 OK * Errors use a non-2xx and (optionally) an error payload. See subsection on \&quot;Error Responses\&quot; for more details.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveRegistrationTest() throws ApiException {
        String registrationId = null;
        RegistrationResponse response = api.retrieveRegistration(registrationId);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Lists all the context provider registrations present in the system.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveRegistrationsTest() throws ApiException {
        Double limit = null;
        Double offset = null;
        String options = null;
        List<RegistrationResponse> response = api.retrieveRegistrations(limit, offset, options);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * Creates a new context provider registration. This is typically used for binding context sources as providers of certain data. The registration is represented by a JSON object as described at the beginning of this section. Response: * Successful operation uses 201 Created * Errors use a non-2xx and (optionally) an error payload. See subsection on \&quot;Error Responses\&quot; for more details.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateRegistrationTest() throws ApiException {
        String registrationId = null;
        Registration body = null;
        api.updateRegistration(registrationId, body);

        // TODO: test validations
    }
    
}