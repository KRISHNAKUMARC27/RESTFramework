package com.example.krish.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.krish.config.FrameworkAndApplicationConfigLoader;
import com.example.krish.model.TMFException;
import com.example.krish.model.TMFExceptionInfo;
import com.example.krish.util.CommonUtils;
import com.example.krish.util.RestGWConstants;

@ComponentScan(basePackages = "com.example.krish")
public class AbstractController {

	private static final Logger logger = LogManager.getLogger(AbstractController.class);

	protected String uriPath = null;

	@Autowired
	protected ExceptionMapper exceptionMapper;

	@Autowired
	protected FrameworkAndApplicationConfigLoader frameworkAndApplicationConfigLoader;

	protected static final String RESTGW_STATUS_PROPERTY = "restgw.traffic.status";
	protected static final String RESTGW_DEFAULT_STATUS = "active";
	protected static final String RESTGW_INHIBIT_STATUS = "inhibit";
	protected static final String DEFAULT_STATUS = "active";

	protected String appStatusProperty = "";
	private String appStatus = "active";

	protected Boolean checkRestgwStatus() {
		String gwStatus = frameworkAndApplicationConfigLoader.getProperty(RESTGW_STATUS_PROPERTY,
				RESTGW_DEFAULT_STATUS);
		logger.debug("Rest gateway status from config is " + gwStatus);
		if (appStatusProperty != "" && appStatusProperty != null) {
			appStatus = frameworkAndApplicationConfigLoader.getProperty(appStatusProperty, DEFAULT_STATUS);
			logger.debug("App {} status from config is {}", appStatusProperty, appStatus);
		}
		if (gwStatus.equals(RESTGW_INHIBIT_STATUS)) {
			return false;
		} else if (appStatus.equals(RESTGW_INHIBIT_STATUS)) {
			return false;
		} else
			return true;
	}

	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	public ResponseEntity<TMFExceptionInfo> handleConstraintViolationException(HttpServletRequest req,
			Exception exception) {
		logger.trace("##### Entering handleConstraintViolationException ####");

		Pair<TMFExceptionInfo, HttpStatus> result = exceptionMapper.getHttpResponseInfo(exception);

		if (result != null) {
			TMFExceptionInfo info = result.getFirst();
			ConstraintViolationException cvException = (ConstraintViolationException) exception;
			Set<ConstraintViolation<?>> constraintViolations = cvException.getConstraintViolations();
			List<String> errors = new ArrayList<String>();
			for (ConstraintViolation<?> cv : constraintViolations) {
				errors.add(cv.getPropertyPath() + ": " + cv.getMessage());
			}
			info.setMessage(errors.toString());
			logger.trace("##### Exiting handleConstraintViolationException ####");
			return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
		} else {
			logger.trace("##### Exiting handleConstraintViolationException ####");
			return handleUnmappedError(exception);
		}
	}

	public ResponseEntity<TMFExceptionInfo> handleUnmappedError(Exception exception) {
		logger.trace("##### Entering handleUnmappedError ####");
		CommonUtils.exceptionPrintStackTrace(logger, exception);
		TMFExceptionInfo info = new TMFExceptionInfo(RestGWConstants.UNMAPPED_ERROR, RestGWConstants.UNMAPPED_ERROR_STR,
				exception.getClass().getName());
		logger.trace("##### Exiting handleUnmappedError ####");
		return new ResponseEntity<>(info, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(TMFException.class)
	public ResponseEntity<TMFExceptionInfo> handleTMFException(HttpServletRequest req, TMFException tmfException) {
		logger.trace("##### Entering handleTMFException ####");

		if (tmfException.getTmfExceptionInfo().getContext() != null) {
			Pair<TMFExceptionInfo, HttpStatus> result = exceptionMapper.getHttpResponseInfo(tmfException);
			if (result != null) {
				TMFExceptionInfo info = result.getFirst();
				info.setMessage(tmfException.getMessage());
				logger.trace("##### Exiting handleTMFException ####");
				return new ResponseEntity<>(info, result.getSecond());
			}
		}
		logger.trace("##### Exiting handleTMFException ####");
		return new ResponseEntity<>(tmfException.getTmfExceptionInfo(), tmfException.getHttpErrorStatus());

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<TMFExceptionInfo> handleException(HttpServletRequest req, Exception exception) {
		logger.trace("##### Entering handleException ####");

		CommonUtils.exceptionPrintStackTrace(logger, exception);
		Pair<TMFExceptionInfo, HttpStatus> result = exceptionMapper.getHttpResponseInfo(exception);

		if (result != null) {
			TMFExceptionInfo info = result.getFirst();

			logger.trace("##### Exiting handleException ####");
			return new ResponseEntity<>(result.getFirst(), result.getSecond());
		} else {
			logger.trace("##### Exiting handleException ####");
			return handleUnmappedError(exception);
		}

	}

}
