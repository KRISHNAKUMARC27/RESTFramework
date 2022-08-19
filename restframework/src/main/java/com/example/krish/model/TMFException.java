package com.example.krish.model;

import org.springframework.http.HttpStatus;


public class TMFException extends Exception{

	private static final long serialVersionUID = -6408213705632158052L;
	
	private HttpStatus httpErrorStatus;
	private TMFExceptionInfo tmfExceptionInfo;
	
	public TMFException(HttpStatus httpErrorStatus, TMFExceptionInfo tmfExceptionInfo) {
		super();
		this.httpErrorStatus = httpErrorStatus;
		this.tmfExceptionInfo = tmfExceptionInfo;
	}
	
	public TMFException(TMFExceptionInfo tmfExceptionInfo) {
		super();
		this.tmfExceptionInfo = tmfExceptionInfo;
	}

	public HttpStatus getHttpErrorStatus() {
		return httpErrorStatus;
	}

	public void setHttpErrorStatus(HttpStatus httpErrorStatus) {
		this.httpErrorStatus = httpErrorStatus;
	}

	public TMFExceptionInfo getTmfExceptionInfo() {
		return tmfExceptionInfo;
	}

	public void setTmfExceptionInfo(TMFExceptionInfo tmfExceptionInfo) {
		this.tmfExceptionInfo = tmfExceptionInfo;
	}

	@Override
	public String toString() {
		return "TMFException [httpErrorStatus=" + httpErrorStatus + ", tmfExceptionInfo=" + tmfExceptionInfo + "]";
	}
	
	
	
}
