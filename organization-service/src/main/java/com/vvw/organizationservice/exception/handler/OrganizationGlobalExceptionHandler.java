package com.vvw.organizationservice.exception.handler;

import com.vvw.organizationservice.exception.OrganizationNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class OrganizationGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {OrganizationNotFoundException.class})
	public ResponseEntity<?> handleCourseNotFound(OrganizationNotFoundException organizationNotFoundException, WebRequest request) {
		return handleExceptionInternal(organizationNotFoundException,
				organizationNotFoundException.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
}