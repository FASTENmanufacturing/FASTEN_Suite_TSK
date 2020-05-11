/*
 * IoT REST OAS
 * Open API Specification REST for IoT event management
 *
 * OpenAPI spec version: 0.0.1-SNAPSHOT
 * Contact: Fasten-wp4@lists.inesctec.pt
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fasten.wp4.iot.kafka.client.api;

import com.fasten.wp4.iot.kafka.client.invoker.ApiException;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for IndexControllerApi
 */
@Ignore
public class IndexControllerApiTest {

    private final IndexControllerApi api = new IndexControllerApi();

    
    /**
     * index
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void indexUsingGETTest() throws ApiException {
        String response = api.indexUsingGET();

        // TODO: test validations
    }
    
    /**
     * index
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void indexUsingGET1Test() throws ApiException {
        String response = api.indexUsingGET1();

        // TODO: test validations
    }
    
    /**
     * onOffListner
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void onOffListnerUsingGETTest() throws ApiException {
        String id = null;
        String response = api.onOffListnerUsingGET(id);

        // TODO: test validations
    }
    
    /**
     * onOffProducer
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void onOffProducerUsingGETTest() throws ApiException {
        String id = null;
        String response = api.onOffProducerUsingGET(id);

        // TODO: test validations
    }
    
}