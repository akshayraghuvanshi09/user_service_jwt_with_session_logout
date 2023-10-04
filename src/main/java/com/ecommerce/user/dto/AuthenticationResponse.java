package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

	private String fullName;
	
	private String accessToken;

	private String tokenExpiration;
	
	private String refreshToken;
	
	private String refreshTokenExpiration;
}
