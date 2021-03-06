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


package com.fasten.wp4.database.client.api;

import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.Delivery;
import com.fasten.wp4.database.client.model.PageOfDelivery;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DeliveryControllerApi
 */
@Ignore
public class DeliveryControllerApiTest {

    private final DeliveryControllerApi api = new DeliveryControllerApi();

    
    /**
     * Create a new delivery cost
     *
     * Also returns the url to created data in header location
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createDeliveryTest() throws ApiException {
        Delivery delivery = null;
        Object response = api.createDelivery(delivery);

        // TODO: test validations
    }
    
    /**
     * Delete the delivery
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteDeliveryTest() throws ApiException {
        Long id = null;
        api.deleteDelivery(id);

        // TODO: test validations
    }
    
    /**
     * List all delivery&#39;s cost and time
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveAllDeliveryTest() throws ApiException {
        List<Delivery> response = api.retrieveAllDelivery();

        // TODO: test validations
    }
    
    /**
     * Find one delivery
     *
     * Also returns a link to retrieve all delivery with rel - all
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDeliveryTest() throws ApiException {
        Long id = null;
        Delivery response = api.retrieveDelivery(id);

        // TODO: test validations
    }
    
    /**
     * Find one delivery by Route
     *
     * Also returns a link to retrieve all delivery with rel - all
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDeliveryByRouteTest() throws ApiException {
        String destination = null;
        String origin = null;
        Delivery response = api.retrieveDeliveryByRoute(destination, origin);

        // TODO: test validations
    }
    
    /**
     * Find one delivery by Route
     *
     * Also returns a link to retrieve all delivery with rel - all
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDeliveryByRouteCodeTest() throws ApiException {
        String destinationCode = null;
        String originCode = null;
        Delivery response = api.retrieveDeliveryByRouteCode(destinationCode, originCode);

        // TODO: test validations
    }
    
    /**
     * List paged
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDeliveryFilteredAndPagedTest() throws ApiException {
        String json = null;
        Integer page = null;
        Integer size = null;
        List<String> sort = null;
        PageOfDelivery response = api.retrieveDeliveryFilteredAndPaged(json, page, size, sort);

        // TODO: test validations
    }
    
    /**
     * Retrive a list of Delivery locations in a Tactical Optimization Study
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDeliveryMatrixByTacticalOptimizationTest() throws ApiException {
        Long id = null;
        List<Delivery> response = api.retrieveDeliveryMatrixByTacticalOptimization(id);

        // TODO: test validations
    }
    
    /**
     * List paged
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDeliveryPagedTest() throws ApiException {
        Integer page = null;
        Integer size = null;
        List<String> sort = null;
        PageOfDelivery response = api.retrieveDeliveryPaged(page, size, sort);

        // TODO: test validations
    }
    
    /**
     * Batch update a list of deliveries
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateDeliveriesTest() throws ApiException {
        List<Delivery> list = null;
        Object response = api.updateDeliveries(list);

        // TODO: test validations
    }
    
    /**
     * Edit the inserted data about delivery costs
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateDeliveryTest() throws ApiException {
        Long id = null;
        Delivery object = null;
        Object response = api.updateDelivery(id, object);

        // TODO: test validations
    }
    
}
