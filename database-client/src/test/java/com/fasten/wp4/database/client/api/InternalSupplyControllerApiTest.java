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
import com.fasten.wp4.database.client.model.InternalSupply;
import com.fasten.wp4.database.client.model.PageOfInternalSupply;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for InternalSupplyControllerApi
 */
@Ignore
public class InternalSupplyControllerApiTest {

    private final InternalSupplyControllerApi api = new InternalSupplyControllerApi();

    
    /**
     * Create a new internal supply
     *
     * Also returns the url to created data in header location
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createInternalSupplyTest() throws ApiException {
        InternalSupply internalSupply = null;
        Object response = api.createInternalSupply(internalSupply);

        // TODO: test validations
    }
    
    /**
     * Delete the internal supply
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deleteInternalSupplyTest() throws ApiException {
        Long id = null;
        api.deleteInternalSupply(id);

        // TODO: test validations
    }
    
    /**
     * List all internal supply
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveAllInternalSupplyTest() throws ApiException {
        List<InternalSupply> response = api.retrieveAllInternalSupply();

        // TODO: test validations
    }
    
    /**
     * Find one internal supply
     *
     * Also returns a link to retrieve all internal supply with rel - all
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void retrieveInternalSupplyTest() throws ApiException {
        Long id = null;
        InternalSupply response = api.retrieveInternalSupply(id);

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
    public void retrieveInternalSupplyFilteredAndPagedTest() throws ApiException {
        String json = null;
        Integer page = null;
        Integer size = null;
        List<String> sort = null;
        PageOfInternalSupply response = api.retrieveInternalSupplyFilteredAndPaged(json, page, size, sort);

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
    public void retrieveInternalSupplyPagedTest() throws ApiException {
        Integer page = null;
        Integer size = null;
        List<String> sort = null;
        PageOfInternalSupply response = api.retrieveInternalSupplyPaged(page, size, sort);

        // TODO: test validations
    }
    
    /**
     * Edit the internal supply
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateInternalSupplyTest() throws ApiException {
        Long id = null;
        InternalSupply object = null;
        Object response = api.updateInternalSupply(id, object);

        // TODO: test validations
    }
    
}
