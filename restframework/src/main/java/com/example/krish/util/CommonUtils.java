package com.example.krish.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.krish.config.ConfigRepository;

public class CommonUtils {
	private static final Logger logger = LogManager.getLogger(CommonUtils.class);
	
	private static int numberOfLinesInStackTrace = 10;
	
	public static String exceptionPrintStackTrace(Logger loger, Throwable exception) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] elements = exception.getStackTrace();
		int lengthCheck = elements.length;
		for(int i= 0; i < lengthCheck && i< numberOfLinesInStackTrace ; i++) {
			sb.append("\t at " + elements[i].toString() + "\n");
		}
		logger.error(exception.getMessage() + "\n " + sb.toString());
		logger.warn(exception.getMessage() + "\n " + sb.toString());
		return sb.toString();
	}

}
