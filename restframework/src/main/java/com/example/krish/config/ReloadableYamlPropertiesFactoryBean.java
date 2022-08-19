package com.example.krish.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;

public class ReloadableYamlPropertiesFactoryBean extends YamlPropertiesFactoryBean{

	public void process(MatchCallback callback) {
		super.process(callback);
	}
}
