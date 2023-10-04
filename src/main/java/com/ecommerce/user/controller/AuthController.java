package com.ecommerce.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.user.AppContext;
import com.ecommerce.user.AppContext.Key;
import com.ecommerce.user.dto.AuthenticationRequest;
import com.ecommerce.user.dto.AuthenticationResponse;
import com.ecommerce.user.dto.RefreshTokenRequest;
import com.ecommerce.user.service.UserSessionService;
import com.ecommerce.user.service.impl.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class AuthController {

	@Autowired
	private final AuthenticationService authenticationService;
	@Autowired
	private final UserSessionService userSessionService;
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
		AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
		return ResponseEntity.ok(authenticationResponse);
	}
	
	@PostMapping("/refreshToken")
	public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
		AuthenticationResponse authenticationResponse = authenticationService.generateRefreshToken(request);
		return ResponseEntity.ok(authenticationResponse);
		
	}
	
//	@PostMapping("/logout")
//	public ResponseEntity<?> logOut(@RequestBody LogoutRequest logoutRequest ,HttpServletRequest request, HttpServletResponse response) {
//		return authenticationService.logout(logoutRequest, request, response);
//	}
	@PostMapping("/logout")
	public ResponseEntity<?> logOut() {
		return authenticationService.logout();
				
	}

}