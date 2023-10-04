package com.ecommerce.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import com.ecommerce.user.AppContext;
import com.ecommerce.user.AppContext.Key;
import com.ecommerce.user.dto.AuthenticationRequest;
import com.ecommerce.user.dto.AuthenticationResponse;
import com.ecommerce.user.dto.LogoutRequest;
import com.ecommerce.user.dto.RefreshTokenRequest;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.UserNotFoundException;
import com.ecommerce.user.payloads.ResponseHandler;
import com.ecommerce.user.payloads.TimeUtil;
import com.ecommerce.user.payloads.TokenBlaklist;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.security.jwt.JwtUtil;
import com.ecommerce.user.service.UserSessionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

	@Autowired
	private final AuthenticationManager authenticationManager;

	@Autowired
	private final UserRepository userRepository;
	
	@Autowired
	private final TokenBlaklist tokenBlaklist;
	
	@Autowired
	private final UserSessionService userSessionService;

	@Autowired
	private final JwtUtil jwtUtil;

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
				() -> new UserNotFoundException("user doest not exist with username : " + request.getEmail()));
		
		String token = jwtUtil.generateToken(user,true);
		String refreshToken = jwtUtil.generateRefreshToken(user);
		return AuthenticationResponse.builder()
					.fullName(user.getFname() + " " + user.getLname())
					.accessToken(token)
					.tokenExpiration(TimeUtil.miliTominuteConvter(jwtUtil.getJwtTokenValidity()))
					.refreshToken(refreshToken)
					.refreshTokenExpiration(TimeUtil.miliTominuteConvter(jwtUtil.getRefreshJwtTokenValidity()))
					.build();
	}

	public AuthenticationResponse generateRefreshToken(RefreshTokenRequest refreshTokenRequest) {
	    String Token = refreshTokenRequest.getRefreshToken();

	    String username = jwtUtil.extractUsername(Token);
	    if (username == null) {
	        throw new UserNotFoundException("Invalid JWT");
	    }

	    User user = getUserByUsername(username);
	    validateRefreshToken(Token, user);

	    String accessToken = jwtUtil.generateToken(user,true);
	    String refreshToken = jwtUtil.generateRefreshToken(user);

	    return AuthenticationResponse.builder()
	            .fullName(user.getFname() + " " + user.getLname())
	            .accessToken(accessToken)
	            .refreshToken(refreshToken)
	            .tokenExpiration(TimeUtil.miliTominuteConvter(jwtUtil.getJwtTokenValidity()))
	            .refreshTokenExpiration(TimeUtil.miliTominuteConvter(jwtUtil.getRefreshJwtTokenValidity()))
	            .build();
	}

	private User getUserByUsername(String username) {
	    return userRepository.findByEmail(username)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	private void validateRefreshToken(String refreshToken, User user) {
	    if (!jwtUtil.validateToken(refreshToken, user)) {
	        throw new RuntimeException("Refresh token is expired");
	    }
	}
	
	
//	public ResponseEntity<?> logout(LogoutRequest logoutRequest,HttpServletRequest request, HttpServletResponse response) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		new SecurityContextLogoutHandler().logout(request, response, authentication);
//		
//		SecurityContext context = SecurityContextHolder.getContext();
//		context.setAuthentication(null);
//		
//		String token = request.getHeader("Authorization");
//		tokenBlaklist.getTokens().add(token);
//		tokenBlaklist.getTokens().add("Bearer "+ logoutRequest.getRefreshToken());
//		return ResponseHandler.responseBuilder("User log out successfully!",HttpStatus.OK,null);
//	}
	public ResponseEntity<?> logout() {
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(null);
		userSessionService.deleteSession(AppContext.get(Key.SESSION_ID));
		return ResponseHandler.responseBuilder("User log out successfully!",HttpStatus.OK,null);
	}
	
}
