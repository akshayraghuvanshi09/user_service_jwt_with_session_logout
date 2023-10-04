package com.ecommerce.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class AuthenticationFailedExceptoin extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public AuthenticationFailedExceptoin(String msg) {
		super(msg);
	}
	
	

}
