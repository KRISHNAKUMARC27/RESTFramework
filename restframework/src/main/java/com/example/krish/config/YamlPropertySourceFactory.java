package com.example.krish.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class YamlPropertySourceFactory implements PropertySourceFactory {
	
	private static final Logger logger = LogManager.getLogger(YamlPropertySourceFactory.class);
	protected YamlReloadablePropertySource propSource = null;
	String filename = null;
	ReloadableYamlPropertiesFactoryBean factory = null;

	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource encodedresource) throws IOException {
		// TODO Auto-generated method stub
		logger.trace("#### entering YamlPropertySourceFactory createPropertySource ####");
		factory = new ReloadableYamlPropertiesFactoryBean();
		factory.setResources(encodedresource.getResource());
		Properties properties = factory.getObject();
		logger.trace("#### exiting createPropertySource ");
		filename = encodedresource.getResource().getFilename();
		
		propSource = new YamlReloadablePropertySource(name, properties, encodedresource.getResource());
		return propSource;
	}

}
