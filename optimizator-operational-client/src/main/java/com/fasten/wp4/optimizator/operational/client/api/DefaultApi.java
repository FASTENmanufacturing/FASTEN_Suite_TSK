/*
 * Allocate Production Order
 * API Description
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fasten.wp4.optimizator.operational.client.api;

import com.fasten.wp4.optimizator.operational.client.invoker.ApiCallback;
import com.fasten.wp4.optimizator.operational.client.invoker.ApiClient;
import com.fasten.wp4.optimizator.operational.client.invoker.ApiException;
import com.fasten.wp4.optimizator.operational.client.invoker.ApiResponse;
import com.fasten.wp4.optimizator.operational.client.invoker.Configuration;
import com.fasten.wp4.optimizator.operational.client.invoker.Pair;
import com.fasten.wp4.optimizator.operational.client.invoker.ProgressRequestBody;
import com.fasten.wp4.optimizator.operational.client.invoker.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import com.fasten.wp4.optimizator.operational.client.model.AllocationResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultApi {
    private ApiClient apiClient;

    public DefaultApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DefaultApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for allocatePost
     * @param orderID Production Order Indentification (optional)
     * @param longitude The delivery longitude location (optional)
     * @param latitude The delivery latitude location (optional)
     * @param quantity The quantity to be allocate (optional)
     * @param part The type of spare part (ex.: Button, Dosing chamber, Grid Air Condition, Support for escalator, Home Lift frame) (optional)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call allocatePostCall(String orderID, String longitude, String latitude, String quantity, String part, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/allocate";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (orderID != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("orderID", orderID));
        if (longitude != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("longitude", longitude));
        if (latitude != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("latitude", latitude));
        if (quantity != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("quantity", quantity));
        if (part != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("part", part));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call allocatePostValidateBeforeCall(String orderID, String longitude, String latitude, String quantity, String part, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        

        com.squareup.okhttp.Call call = allocatePostCall(orderID, longitude, latitude, quantity, part, progressListener, progressRequestListener);
        return call;

    }

    /**
     *  allocate production order
     * 
     * @param orderID Production Order Indentification (optional)
     * @param longitude The delivery longitude location (optional)
     * @param latitude The delivery latitude location (optional)
     * @param quantity The quantity to be allocate (optional)
     * @param part The type of spare part (ex.: Button, Dosing chamber, Grid Air Condition, Support for escalator, Home Lift frame) (optional)
     * @return AllocationResult
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public AllocationResult allocatePost(String orderID, String longitude, String latitude, String quantity, String part) throws ApiException {
        ApiResponse<AllocationResult> resp = allocatePostWithHttpInfo(orderID, longitude, latitude, quantity, part);
        return resp.getData();
    }

    /**
     *  allocate production order
     * 
     * @param orderID Production Order Indentification (optional)
     * @param longitude The delivery longitude location (optional)
     * @param latitude The delivery latitude location (optional)
     * @param quantity The quantity to be allocate (optional)
     * @param part The type of spare part (ex.: Button, Dosing chamber, Grid Air Condition, Support for escalator, Home Lift frame) (optional)
     * @return ApiResponse&lt;AllocationResult&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<AllocationResult> allocatePostWithHttpInfo(String orderID, String longitude, String latitude, String quantity, String part) throws ApiException {
        com.squareup.okhttp.Call call = allocatePostValidateBeforeCall(orderID, longitude, latitude, quantity, part, null, null);
        Type localVarReturnType = new TypeToken<AllocationResult>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     *  allocate production order (asynchronously)
     * 
     * @param orderID Production Order Indentification (optional)
     * @param longitude The delivery longitude location (optional)
     * @param latitude The delivery latitude location (optional)
     * @param quantity The quantity to be allocate (optional)
     * @param part The type of spare part (ex.: Button, Dosing chamber, Grid Air Condition, Support for escalator, Home Lift frame) (optional)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call allocatePostAsync(String orderID, String longitude, String latitude, String quantity, String part, final ApiCallback<AllocationResult> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = allocatePostValidateBeforeCall(orderID, longitude, latitude, quantity, part, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<AllocationResult>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
