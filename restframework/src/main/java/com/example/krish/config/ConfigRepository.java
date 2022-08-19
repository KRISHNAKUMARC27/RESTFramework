package com.example.krish.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import com.example.krish.util.BeanUtil;

@Repository
public class ConfigRepository {
	private static final Logger logger = LogManager.getLogger(ConfigRepository.class);
	
	protected static Map <String , ReloadablePropertySource> preInitializationMap = new HashMap<>();
	
	private static boolean isAppStarted = false;
	
	protected Map <String, ReloadablePropertySource> configMap = new HashMap<>();
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	public Map<String, ReloadablePropertySource> getConfigMap(){
		return configMap;
	}
	
	public static synchronized void registerConfig(ReloadablePropertySource propSource) {
		ConfigRepository configRepository = BeanUtil.getBean(ConfigRepository.class);
		if(configRepository != null && isAppStarted) {
			configRepository._registerConfig(propSource);
		} else {
			synchronized (preInitializationMap) {
				
				preInitializationMap.put(propSource.getName(), propSource);
			}
			logger.info("Added property Soruce to preInitMap " + propSource.getName());
		}
		
	}

	public synchronized void _registerConfig(ReloadablePropertySource propSource) {
		logger.traceEntry();
		configMap.put(propSource.getName(), propSource);
		logger.traceExit();
	}
	
	public synchronized boolean refreshConfig(String name) {
		ReloadablePropertySource propSource = configMap.get(name);
		if(propSource!=null)
			return propSource.reload();
		
		return false;
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public synchronized void completeInitialization() {
		isAppStarted = true;
		
		synchronized(preInitializationMap) {
			for(String propSourceName : preInitializationMap.keySet())
				_registerConfig(preInitializationMap.get(propSourceName));
			preInitializationMap.clear();
		}
		logger.info("Publishing configRepositoryEvent");
		ConfigRepositoryEvent configRepositoryEvent = new ConfigRepositoryEvent(configMap);
		applicationEventPublisher.publishEvent(configRepositoryEvent);
	}
}
