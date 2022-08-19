package com.example.krish.config;

import org.springframework.context.ApplicationEvent;

public class ConfigRepositoryEvent extends ApplicationEvent {
	
	public ConfigRepositoryEvent(Object source) {
		super(source);
	}
}
