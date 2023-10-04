package com.ecommerce.user.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.user.exception.UserAlreadyExistException;
import com.ecommerce.user.exception.UserNotFoundException;
import com.ecommerce.user.payloads.ResponseHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({UserNotFoundException.class, UserAlreadyExistException.class})
	public ResponseEntity<?> UserExceptionHandler(Exception exception){
		exception.printStackTrace();
		return ResponseHandler.responseBuilder(exception.getMessage(), HttpStatus.NOT_FOUND, null);
	}
}
