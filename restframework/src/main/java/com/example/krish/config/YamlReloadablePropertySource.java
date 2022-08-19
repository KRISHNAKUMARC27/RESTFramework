package com.example.krish.config;

import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;

import com.example.krish.util.BeanUtil;

public class YamlReloadablePropertySource extends PropertiesPropertySource implements ReloadablePropertySource{
	
	private static final Logger logger = LogManager.getLogger(YamlReloadablePropertySource.class);
	
	Resource resource;
	
	protected YamlReloadablePropertySource(String name, Properties source , Resource resource) {
		super(name, source);
		this.resource = resource;
		ConfigRepository.registerConfig(this);
	}

	
	public synchronized boolean reload() {
		logger.traceEntry();
		logger.info("reloading for propertysource " + name);
		try {
			this.source.clear();
			ReloadableYamlPropertiesFactoryBean factory = new ReloadableYamlPropertiesFactoryBean();
			factory.setResources(resource);
			Properties result = factory.getObject();
			factory.process((properties, map) -> result.putAll(properties));
			source.putAll((Map) result);
			
			//Get the original that had property source annotation with this property source name
			String beanName = BeanUtil.getBeanNameWithPropertySource(name);
			if(beanName != null) {
				logger.traceExit();
				return BeanUtil.reload(beanName);
			}
			logger.info("Bean not found for reloading for propertySoruce " + name);
		} catch (Exception x) {
			logger.error("Unable to reload property Soruce " + name, x);
		}
		logger.traceExit();
		return false;
		
	}
	
}
