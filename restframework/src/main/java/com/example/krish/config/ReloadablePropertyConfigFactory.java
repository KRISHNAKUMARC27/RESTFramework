package com.example.krish.config;

import java.io.IOException;

import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.Nullable;

public class ReloadablePropertyConfigFactory extends DefaultPropertySourceFactory{
	
	@Override
	public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
		ReloadableResourcePropertySource propSource = name!= null ?
				new ReloadableResourcePropertySource(name, resource) :
					new ReloadableResourcePropertySource(resource);
		return propSource;
	}

}
