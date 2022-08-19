package com.example.krish.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("exceptions")
@PropertySource(name = "restgw.exceptions_config", value = {
		"${restgw.exception.config.file:classpath:restgw.exception.yml}" }, ignoreResourceNotFound = true, factory = YamlPropertySourceFactory.class)
//@PropertySource(name = "restgw.local.exceptions_config", value = {
//		"${restgw.local.exception.config.file:classpath:restgw.exception.local.yml}" }, ignoreResourceNotFound = true, factory = YamlPropertySourceFactory.class)
public class ExceptionConfigLoader extends YamlConfigLoader<ExceptionConfig> {

	private static final Logger logger = LogManager.getLogger(ExceptionConfigLoader.class);

	protected Map<String, ExceptionConfig> exceptionLookup = new HashMap<>();

	@Override
	public void merge() {
		exceptionLookup.clear();
		Consumer<ExceptionConfig> mergeAction = (exceptionConfig) -> {
			String exceptioKey = exceptionConfig.getName();

			if (!(exceptionConfig.getContext() == null || exceptionConfig.getContext().equals(""))) {
				exceptioKey = exceptioKey.concat("." + exceptionConfig.getContext());
			}
			exceptionLookup.put(exceptioKey, exceptionConfig);
		};
		restgw.forEach(mergeAction);
		restgw_local.forEach(mergeAction);
		logger.info(exceptionLookup);
	}

	public ExceptionConfig getExceptionConfig(Exception x) {
		return exceptionLookup.get(x.getClass().getName());
	}

	public ExceptionConfig getExceptionConfigWithContext(Exception x, String context) {
		return exceptionLookup.get(x.getClass().getName() + "." + context);
	}

	@Override
	public String toString() {
		return "ExceptionConfigLoader [exceptionLookup=" + exceptionLookup + "]";
	}
	
	

}
