package com.example.krish.controller;

import java.util.HashMap;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.krish.model.RestAPIResponse;
import com.example.krish.model.TMFExceptionInfo;

@ControllerAdvice(value="com.example")
public class CRUDControllerAdvice implements ResponseBodyAdvice<Object>{

	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	
	public static final String URL_ERROR_CODE = "0000";
	
	public static final int PUT_POST = 1;
	public static final int GET = 2;
	public static final int DELETE = 3;
	
	public int iCurdOper = 0;
	public String uriPath = null;
	
	public String subProcess = " ";
	public String servedApp = null;
	public int status = -1;
	
	public String restGwCode = null;
	
	public ServerHttpRequest reqObj = null;
	public ServerHttpResponse respObj = null;
	
	public static final HashMap<String,String> EMPTY = new HashMap<String,String>();
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		
		final RestAPIResponse output = new RestAPIResponse();
		
		status = -1;
		String method = null;
		uriPath = null;
		
		restGwCode = null;
		
		reqObj = request;
		respObj = response;
		
		if( response instanceof ServletServerHttpResponse) {
			status = ((ServletServerHttpResponse)response).getServletResponse().getStatus();
			
			method = request.getMethodValue();
			uriPath = request.getURI().getPath();
		}
		
		if(body instanceof TMFExceptionInfo) {
			output.setStatus(false);
			output.setDescription(FAILURE);
			output.setError(body);
			output.setData(EMPTY);
		} else if(body instanceof Throwable) {
			output.setStatus(false);
			output.setDescription(FAILURE);
			output.setError(body);
			output.setData(EMPTY);
		} else if(body instanceof MappingJacksonValue) {
			output.setStatus(true);
			output.setDescription(SUCCESS);
			output.setError(EMPTY);
			output.setData(((MappingJacksonValue)body).getValue());
		} else if(body instanceof Boolean) {
			boolean result = (boolean) body;
			
			output.setStatus(result);
			if(result)
				output.setDescription(SUCCESS);
			else
				output.setDescription(FAILURE);
			output.setError(EMPTY);
			output.setData(EMPTY);
		} else {
			output.setStatus(true);
			output.setDescription(SUCCESS);
			if(status >= 400) {
				output.setStatus(false);
				output.setDescription(FAILURE);
			}
			if(output.isStatus()) {
				if(body == null)
					output.setData(EMPTY);
				else
					output.setData(body);
				
				output.setError(EMPTY);
			} else {
				output.setData(EMPTY);
				if(body == null)
					output.setError(EMPTY);
				else
					output.setError(body);
			}
		}
		
		return output;
	}

	
	
	
}
