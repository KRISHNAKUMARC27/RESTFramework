package com.example.krish.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.krish.config.ExceptionConfig;
import com.example.krish.config.ExceptionConfigLoader;
import com.example.krish.model.TMFException;
import com.example.krish.model.TMFExceptionInfo;
import com.example.krish.util.BeanUtil;

@Configuration
public class ExceptionMapper {
	
	private static final Logger logger = LogManager.getLogger(ExceptionMapper.class);
	
	public Pair<TMFExceptionInfo, HttpStatus> getHttpResponseInfo(Exception exception){
		logger.trace("#### Entering ExceptionMapper getHttpResponseInfo");
		
		ExceptionConfigLoader exceptionConfigLoader = BeanUtil.getBean(ExceptionConfigLoader.class);
		ExceptionConfig config = null;
		
		if(exception instanceof TMFException)
			config = exceptionConfigLoader.getExceptionConfigWithContext(exception, ((TMFException) exception).getTmfExceptionInfo().getContext());
		else
			config = exceptionConfigLoader.getExceptionConfig(exception);
		
		if(config!=null) {
			TMFExceptionInfo tmfExceptionInfo = new TMFExceptionInfo(config.getCode(), config.getReason());
			HttpStatus httpStatus = HttpStatus.valueOf(config.getHttpStatus());
			Pair<TMFExceptionInfo, HttpStatus> result = Pair.of(tmfExceptionInfo, httpStatus);
			
			logger.trace("exiting getHttpResponseInfo");
			return result;
		}
		logger.trace("exiting getHttpResponseInfo config null");
		return null;
	}

}
