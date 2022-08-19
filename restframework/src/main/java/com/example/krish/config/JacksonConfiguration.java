package com.example.krish.config;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Configuration
public class JacksonConfiguration {
	
	protected ObjectMapper objectMapper = null;
	
	public JacksonConfiguration(ObjectMapper objectMapper) {
		objectMapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		this.objectMapper = objectMapper;
	}
	
	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}
}
