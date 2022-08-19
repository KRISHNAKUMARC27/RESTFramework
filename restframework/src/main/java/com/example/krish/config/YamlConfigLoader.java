package com.example.krish.config;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class YamlConfigLoader<T> {

	private static final Logger logger = LogManager.getLogger(YamlConfigLoader.class);
	
	protected Set<T> mergedConfig = new HashSet<>();
	
	protected Set<T> restgw = new HashSet<>();
	protected Set<T> restgw_local = new HashSet<>();

	public static ApplicationContext context;

	public void setRestgw(Set<T> restgw) {
		this.restgw = restgw;
		merge();
	}

	public void setRestgw_local(Set<T> restgw_local) {
		this.restgw_local = restgw_local;
		merge();
	}

	// this merge will work only when T implements equals and hash code 
	// the set will add a new element only when it conclude element is not present
	// hence all the sets are merged in reverse order
	public void merge() {
		// TODO Auto-generated method stub
		mergedConfig.clear();
		mergedConfig.addAll(restgw_local);
		mergedConfig.addAll(restgw);
		
	}

	@Override
	public String toString() {
		return "YamlConfigLoader [mergedConfig=" + mergedConfig + "]";
	}
	
	
	
}
