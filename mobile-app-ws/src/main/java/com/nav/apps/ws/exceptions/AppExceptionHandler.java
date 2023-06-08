package com.nav.apps.ws.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nav.apps.ws.ui.model.CustomErrorMessage;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value= {Exception.class})
	public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest req) {
		
		String errorMessage = ex.getLocalizedMessage();
		if (errorMessage == null) errorMessage = ex.toString();
		CustomErrorMessage customErrorMessage = new CustomErrorMessage(new Date(), errorMessage);
//		return new ResponseEntity<>(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(customErrorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@ExceptionHandler(value= {NullPointerException.class})
//	public ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest req) {
//		
//		String errorMessage = ex.getLocalizedMessage();
//		if (errorMessage == null) errorMessage = ex.toString();
//		CustomErrorMessage customErrorMessage = new CustomErrorMessage(new Date(), errorMessage);
//		return new ResponseEntity<>(customErrorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//
//	@ExceptionHandler(value= {UserServiceException.class})
//	public ResponseEntity<Object> handleNullPointerException(UserServiceException ex, WebRequest req) {
//		
//		String errorMessage = ex.getLocalizedMessage();
//		if (errorMessage == null) errorMessage = ex.toString();
//		CustomErrorMessage customErrorMessage = new CustomErrorMessage(new Date(), errorMessage);
//		return new ResponseEntity<>(customErrorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	// Combining above 2 exception handler into one

	@ExceptionHandler(value= {NullPointerException.class, UserServiceException.class})
	public ResponseEntity<Object> handleNullPointerException(Exception ex, WebRequest req) {
		
		String errorMessage = ex.getLocalizedMessage();
		if (errorMessage == null) errorMessage = ex.toString();
		CustomErrorMessage customErrorMessage = new CustomErrorMessage(new Date(), errorMessage);
		return new ResponseEntity<>(customErrorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
