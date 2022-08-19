package com.example.krish.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@PropertySource(name = "restgw.config", value = {
		"${restgw.config.file:classpath:restgw.config.properties}" }, ignoreResourceNotFound = true, factory = ReloadablePropertyConfigFactory.class)
//@PropertySource(name = "restgw.local.config", value = {
//		"${local.restgw.config.file:classpath:restgw.config.local.properties}" }, ignoreResourceNotFound = true, factory = ReloadablePropertyConfigFactory.class)
@Configuration
public class FrameworkAndApplicationConfigLoader {
	private static final Logger logger = LogManager.getLogger(FrameworkAndApplicationConfigLoader.class);
	
	@Autowired
	private Environment env;
	
	public String getProperty(String key) {
		return env.getProperty(key);
	}
	
	public String getProperty(String key, String defaultValue) {
		String propvalue = env.getProperty(key);
		if(propvalue != null)
			return propvalue;
		else
			return defaultValue;
	}
	
	public Integer getIntegerProperty(String key) {
		String propvalue = env.getProperty(key);
		int value = 0;
		if(propvalue != null) {
			try {
				value = Integer.parseInt(env.getProperty(key));
			} catch(Exception e) {
				logger.warn("Exception in getIntegerProperty " + e);
			}
		}
		return value;
	}
	
	public Integer getIntegerProperty(String key, int defaultValue) {
		String propvalue = env.getProperty(key);
		int value = 0;
		if(propvalue != null) {
			try {
				value = Integer.parseInt(env.getProperty(key));
			} catch(Exception e) {
				value = defaultValue;
				logger.warn("Exception in getIntegerProperty " + e);
			}
			return value;
		} else
			return defaultValue;
	}

}
