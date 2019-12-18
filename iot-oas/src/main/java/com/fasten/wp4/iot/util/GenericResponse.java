package com.fasten.wp4.iot.util;

import java.util.List;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "message", "error"})
public class GenericResponse {
	
	@JsonProperty("message")
    private String message;
	
	@JsonProperty("error")
	private String error;

    public GenericResponse() {
    }
    
    @JsonIgnore
    public GenericResponse( String message) {
        super();
        this.message = message;
    }

    @JsonIgnore
    public GenericResponse( String message,  String error) {
        super();
        this.message = message;
        this.error = error;
    }

    @JsonIgnore
    public GenericResponse(List<ObjectError> allErrors, String error) {
        this.error = error;
        
        ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
        
        allErrors.stream().forEach(e -> {
            if (e instanceof FieldError) {
            		ObjectNode node = JsonNodeFactory.instance.objectNode(); // initializing
            		node.put("field",((FieldError) e).getField());
            		node.put("defaultMessage",e.getDefaultMessage());
            		arrayNode.add(node);
//                return "{\"field\":\"" + ((FieldError) e).getField() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
            } else {
	            	ObjectNode node = JsonNodeFactory.instance.objectNode(); // initializing
	            	node.put("object",e.getObjectName());
	            	node.put("defaultMessage",e.getDefaultMessage());
	            	arrayNode.add(node);
//                return "{\"object\":\"" + e.getObjectName() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
            }
        });
        ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(arrayNode);
			this.message = json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage( String message) {
        this.message = message;
    }

    @JsonProperty("error")
    public String getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError( String error) {
        this.error = error;
    }

}
