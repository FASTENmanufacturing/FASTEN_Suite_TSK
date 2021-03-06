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
import com.fasten.wp4.database.client.model.DistributionCenter;
import com.fasten.wp4.database.client.model.PageOfDistributionCenter;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DistributionCenterControllerApi
 */
@Ignore
public class DistributionCenterControllerApiTest {

    private final DistributionCenterControllerApi api = new DistributionCenterControllerApi();

    
    /**
     * Register Distribution Center
     *
     * Also returns the url to created data in header location
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createDistributionCenterTest() throws ApiException {
        DistributionCenter distributionCenter = null;
        Object response = api.createDistributionCenter(distributionCenter);

        // TODO: test validations
    }
    
    /**
     * Delete the Distribution Center
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteDistributionCenterTest() throws ApiException {
        Long id = null;
        api.deleteDistributionCenter(id);

        // TODO: test validations
    }
    
    /**
     * List all Distribution Centers
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveAllDistributionCenterTest() throws ApiException {
        List<DistributionCenter> response = api.retrieveAllDistributionCenter();

        // TODO: test validations
    }
    
    /**
     * Find distribution centers by name
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveAllDistributionCenterByNameTest() throws ApiException {
        List<String> response = api.retrieveAllDistributionCenterByName();

        // TODO: test validations
    }
    
    /**
     * Retrive one Distribution Center
     *
     * Also returns a link to retrieve all distribution center with rel - all
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDistributionCenterTest() throws ApiException {
        Long id = null;
        DistributionCenter response = api.retrieveDistributionCenter(id);

        // TODO: test validations
    }
    
    /**
     * Retrive Distribution Centers by city name
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDistributionCenterByCityNameTest() throws ApiException {
        String city = null;
        List<DistributionCenter> response = api.retrieveDistributionCenterByCityName(city);

        // TODO: test validations
    }
    
    /**
     * Retrive one Distribution Center by code
     *
     * Also returns a link to retrieve all distribution center with rel - all
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDistributionCenterByCodeTest() throws ApiException {
        String code = null;
        DistributionCenter response = api.retrieveDistributionCenterByCode(code);

        // TODO: test validations
    }
    
    /**
     * Retrive Distribution Centers by name without space and uppercased
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDistributionCenterByExcellNameTest() throws ApiException {
        String excellName = null;
        DistributionCenter response = api.retrieveDistributionCenterByExcellName(excellName);

        // TODO: test validations
    }
    
    /**
     * Retrive one Distribution Center by name
     *
     * Also returns a link to retrieve all distribution center with rel - all
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDistributionCenterByNameTest() throws ApiException {
        String name = null;
        DistributionCenter response = api.retrieveDistributionCenterByName(name);

        // TODO: test validations
    }
    
    /**
     * Find distribution centers by priority
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDistributionCenterByPriorityTest() throws ApiException {
        String priority = null;
        List<DistributionCenter> response = api.retrieveDistributionCenterByPriority(priority);

        // TODO: test validations
    }
    
    /**
     * Retrive a list of Distribution Center candidates in a Tactical Optimization Study
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveDistributionCenterByTacticalOptimizationTest() throws ApiException {
        Long id = null;
        List<DistributionCenter> response = api.retrieveDistributionCenterByTacticalOptimization(id);

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
    public void retrieveDistributionCenterFilteredAndPagedTest() throws ApiException {
        String json = null;
        Integer page = null;
        Integer size = null;
        List<String> sort = null;
        PageOfDistributionCenter response = api.retrieveDistributionCenterFilteredAndPaged(json, page, size, sort);

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
    public void retrieveDistributionCenterPagedTest() throws ApiException {
        Integer page = null;
        Integer size = null;
        List<String> sort = null;
        PageOfDistributionCenter response = api.retrieveDistributionCenterPaged(page, size, sort);

        // TODO: test validations
    }
    
    /**
     * Update a Distribution Center
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateDistributionCenterTest() throws ApiException {
        Long id = null;
        DistributionCenter object = null;
        Object response = api.updateDistributionCenter(id, object);

        // TODO: test validations
    }
    
}
