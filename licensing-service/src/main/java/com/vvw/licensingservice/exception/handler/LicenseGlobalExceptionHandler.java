package com.vvw.licensingservice.exception.handler;

import com.vvw.licensingservice.exception.LicenseNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class LicenseGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = {LicenseNotFoundException.class})
	public ResponseEntity<?> handleCourseNotFound(LicenseNotFoundException exception, WebRequest request) {
		return handleExceptionInternal(exception,
				exception.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
}
