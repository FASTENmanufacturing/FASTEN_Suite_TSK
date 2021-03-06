/*
 * Email OAS
 * Open API Specification for FASTEN Holistic Simulator-Optimizer Tool
 *
 * OpenAPI spec version: 0.0.1-SNAPSHOT
 * Contact: fasten.suite@gmail.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fasten.wp4.email.client.api;

import com.fasten.wp4.email.client.invoker.ApiException;
import com.fasten.wp4.email.client.model.Email;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for EmailControllerApi
 */
@Ignore
public class EmailControllerApiTest {

    private final EmailControllerApi api = new EmailControllerApi();

    
    /**
     * Send templated message
     *
     * Uses MimeMessageHelper and Template form Freemarker
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void sendWithFreemarkerUsingPOSTTest() throws ApiException {
        Email email = null;
        api.sendWithFreemarkerUsingPOST(email);

        // TODO: test validations
    }
    
    /**
     * Send mime message
     *
     * Uses MimeMessageHelper from java
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void sendWithMimeMessageHelperUsingPOSTTest() throws ApiException {
        Email email = null;
        api.sendWithMimeMessageHelperUsingPOST(email);

        // TODO: test validations
    }
    
    /**
     * Send mime message
     *
     * Uses MimeMessagePreparator from java
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void sendWithMimeMessagePreparatorUsingPOSTTest() throws ApiException {
        Email email = null;
        api.sendWithMimeMessagePreparatorUsingPOST(email);

        // TODO: test validations
    }
    
    /**
     * Send simple message
     *
     * Uses SimpleMailMessage from java
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void sendWithSimpleMessageUsingPOSTTest() throws ApiException {
        Email email = null;
        api.sendWithSimpleMessageUsingPOST(email);

        // TODO: test validations
    }
    
}
