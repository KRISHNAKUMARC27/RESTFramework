package com.example.krish.model;

import lombok.Data;

@Data
public class RestAPIResponse {
	
	protected boolean status;
	protected String description;
	protected Object data;
	protected Object error;

}
