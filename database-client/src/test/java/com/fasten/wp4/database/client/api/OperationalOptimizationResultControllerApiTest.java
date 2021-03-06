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
import com.fasten.wp4.database.client.model.OperationalOptimizationResult;
import com.fasten.wp4.database.client.model.PageOfOperationalOptimizationResult;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for OperationalOptimizationResultControllerApi
 */
@Ignore
public class OperationalOptimizationResultControllerApiTest {

    private final OperationalOptimizationResultControllerApi api = new OperationalOptimizationResultControllerApi();

    
    /**
     * Save a operationalOptimizationResult
     *
     * Also returns the url to created data in header location 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createOperationalOptimizationResultTest() throws ApiException {
        OperationalOptimizationResult operationalOptimizationResult = null;
        Object response = api.createOperationalOptimizationResult(operationalOptimizationResult);

        // TODO: test validations
    }
    
    /**
     * Save a results list
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createOperationalOptimizationResultsTest() throws ApiException {
        List<OperationalOptimizationResult> operationalOptimizationResults = null;
        Object response = api.createOperationalOptimizationResults(operationalOptimizationResults);

        // TODO: test validations
    }
    
    /**
     * Delete a operationalOptimizationResult
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteOperationalOptimizationResultTest() throws ApiException {
        Long id = null;
        api.deleteOperationalOptimizationResult(id);

        // TODO: test validations
    }
    
    /**
     * Find boolean by study
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void existsByOperationalOptimizationTest() throws ApiException {
        Long id = null;
        Boolean response = api.existsByOperationalOptimization(id);

        // TODO: test validations
    }
    
    /**
     * List all
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveAllOperationalOptimizationResultTest() throws ApiException {
        List<OperationalOptimizationResult> response = api.retrieveAllOperationalOptimizationResult();

        // TODO: test validations
    }
    
    /**
     * Find by study
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveByOperationalOptimizationTest() throws ApiException {
        Long id = null;
        List<OperationalOptimizationResult> response = api.retrieveByOperationalOptimization(id);

        // TODO: test validations
    }
    
    /**
     * Find by orderID
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveByOrderIDTest() throws ApiException {
        String orderID = null;
        List<OperationalOptimizationResult> response = api.retrieveByOrderID(orderID);

        // TODO: test validations
    }
    
    /**
     * Find a operationalOptimizationResult
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveOperationalOptimizationResultTest() throws ApiException {
        Long id = null;
        OperationalOptimizationResult response = api.retrieveOperationalOptimizationResult(id);

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
    public void retrieveOperationalOptimizationResultFilteredAndPagedTest() throws ApiException {
        String json = null;
        Integer page = null;
        Integer size = null;
        List<String> sort = null;
        PageOfOperationalOptimizationResult response = api.retrieveOperationalOptimizationResultFilteredAndPaged(json, page, size, sort);

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
    public void retrieveOperationalOptimizationResultPagedTest() throws ApiException {
        Integer page = null;
        Integer size = null;
        List<String> sort = null;
        PageOfOperationalOptimizationResult response = api.retrieveOperationalOptimizationResultPaged(page, size, sort);

        // TODO: test validations
    }
    
    /**
     * Update a operationalOptimizationResult
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateOperationalOptimizationResultTest() throws ApiException {
        Long id = null;
        OperationalOptimizationResult object = null;
        Object response = api.updateOperationalOptimizationResult(id, object);

        // TODO: test validations
    }
    
}
