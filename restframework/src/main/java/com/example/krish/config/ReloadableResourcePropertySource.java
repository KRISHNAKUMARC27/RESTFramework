package com.example.krish.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePropertySource;

public class ReloadableResourcePropertySource extends ResourcePropertySource implements ReloadablePropertySource {
    
	private static final Logger logger = LogManager.getLogger(ReloadableResourcePropertySource.class);

	EncodedResource resource;
	
	public ReloadableResourcePropertySource(EncodedResource resource) throws IOException {
		super(resource);
		this.resource = resource;
	}
	
	public ReloadableResourcePropertySource(String name, EncodedResource resource) throws IOException {
		super(name, resource);
		this.resource = resource;
		ConfigRepository.registerConfig(this);
	}
	
	public synchronized boolean reload() {
		logger.traceEntry();
		Properties properties = null;
		
		try {
			properties = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			logger.trace("Unable to load property soruce " + name, e);
			logger.traceExit();
			return false;
		}
		
		this.source.clear();
		if(properties!=null) {
			this.source.putAll((Map)properties);
			logger.traceExit();
			return true;
		}
		logger.traceExit();
		return false;
	}
}
