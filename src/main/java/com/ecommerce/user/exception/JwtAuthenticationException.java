package com.ecommerce.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class JwtAuthenticationException extends AuthenticationException {

	public JwtAuthenticationException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;
}