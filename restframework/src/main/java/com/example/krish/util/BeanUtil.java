package com.example.krish.util;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class BeanUtil implements ApplicationContextAware, BeanFactoryAware {

	private static final Logger logger = LogManager.getLogger(BeanUtil.class);

	private static final String EXAMPLE_PKG = "com.example";

	private static ApplicationContext context;
	private static DefaultListableBeanFactory beanFactory;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static <T> T getBean(Class<T> beanClass) {
		if (context != null) {
			return context.getBean(beanClass);
		}
		logger.error("Context is not yet inialized");
		return null;
	}

	public static boolean reload(String beanName) {
		if (context != null) {
			beanFactory.destroyBean(beanName);
			Object bean = beanFactory.getBean(beanName);
			if (bean != null) {
				return true;
			}
		} else {
			logger.error("Context is yet not inialized");
		}
		return false;
	}

	// TODO return as string array in case developer uses same property source name
	// for multiple config classes

	public static String getBeanNameWithPropertySource(String propSourceName) {
		logger.traceEntry();
		logger.debug("getting bean name for " + propSourceName);
		if (context == null) {
			logger.error("Context is not yet initializded");
			return null;
		}

		Map<String, Object> beans = context.getBeansWithAnnotation(PropertySource.class);

		for (Map.Entry<String, Object> entry : beans.entrySet()) {
			String beanName = entry.getKey();
			Object bean = entry.getValue();

			Class<?> beanClass = bean.getClass();
			if (!beanClass.getName().startsWith(EXAMPLE_PKG))
				continue;

			// String adds some dynamic inner class
			beanClass = getBeanClass(bean);

			Annotation[] annotations = beanClass.getAnnotationsByType(PropertySource.class);

			for (Annotation annotation : annotations) {
				if (annotation instanceof PropertySource) {
					PropertySource propSource = (PropertySource) annotation;
					if (propSource.name() != null && propSource.name().equals(propSourceName)) {
						logger.debug("bean name returned is " + beanName);
						logger.traceExit();
						return beanName;
					}
				}
			}
		}
		logger.info("could not find bean for property source" + propSourceName);
		logger.traceExit();
		return null;

	}
	
	public static Class getBeanClass(Object bean) {
		Class beanClass = bean.getClass();
		
		String className = beanClass.getName();
		
		if(className.contains("$")) {
			className = className.substring(0, className.indexOf("$"));
			try {
				beanClass = Class.forName(className);
			} catch(Exception x) {
				return null;
			}
		}
		return beanClass;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}
}
