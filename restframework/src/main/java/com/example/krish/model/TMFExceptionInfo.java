package com.example.krish.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TMFExceptionInfo {

	private String code;
	private String reason;
	private String message;
	private Object resource;
	
	@JsonIgnore
	private String context;
	
	public TMFExceptionInfo() {
		
	}
	
	public TMFExceptionInfo(String code, String reason, String message) {
		this(code, reason, message, null);
	}
	
	public TMFExceptionInfo(String code, String reason) {
		this(code, reason, null);
	}
	
	public TMFExceptionInfo(String code, String reason, String message, String context) {
		this.code = code;
		this.reason = reason;
		this.message = message;
		this.context = context;
	}
	
	public TMFExceptionInfo(String context) {
		this(null,null,null,context);
	}
}
