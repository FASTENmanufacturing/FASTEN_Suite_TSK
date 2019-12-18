/*
 * Kafka Response OAS
 * Open API Specification REST for FASTEN Holistic Simulator-Optimizer Tool
 *
 * OpenAPI spec version: 0.0.1-SNAPSHOT
 * Contact: Fasten-wp4@lists.inesctec.pt
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fasten.wp4.kafka.response.client.api;

import com.fasten.wp4.kafka.response.client.invoker.ApiException;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for KafkaResponseControllerApi
 */
@Ignore
public class KafkaResponseControllerApiTest {

    private final KafkaResponseControllerApi api = new KafkaResponseControllerApi();

    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void kafkaTopicNameGetTest() throws ApiException {
        String topicName = null;
        api.kafkaTopicNameGet(topicName);

        // TODO: test validations
    }
    
}
