package com.example.krish.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionConfig {
	private String name;
	private Integer httpStatus;
	private String code;
	private String reason;
	private String context;
}
