package com.fasten.wp4.optimizator.tactical.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class ObjectMapperConfig {

	@Bean 
	public ObjectMapper objectMapper() {
		return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
								 .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
}
