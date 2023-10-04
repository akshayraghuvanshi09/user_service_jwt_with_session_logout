package com.ecommerce.user.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.service.UserSessionService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
@Service
public class JwtUtil {
	
	private static final String SESSION_ID = "SESSION_ID";
	
	@Autowired
	private UserSessionService userSessionService;

    @Value("${spring.jwtConfig.token-validity}")
    private long jwtTokenValidity;

    @Value("${spring.jwtConfig.refresh-token-validity}")
    private long refreshJwtTokenValidity;

    @Value("${spring.jwtConfig.secret-key}")
    private String secretKey;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateRefreshToken(claims, userDetails.getUsername());
    }

    private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshJwtTokenValidity))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public String generateToken(User user, boolean session) {
        Map<String, Object> claims = new HashMap<>();
        if(session) {
        	claims.put(SESSION_ID, userSessionService.saveSession(user));
        }else {
        	String sessionId = userSessionService.getSession(user.getId()).getId();
        	claims.put(SESSION_ID, sessionId);
        	if(!userSessionService.validateSession(sessionId)) {
        		throw new RuntimeException("Session Already Expired...");
        	}
        }
        return createToken(claims, user.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public String extractSessionId(String token) {
    	Claims claims = extractAllClaims(token);
    	Object object = claims.get(SESSION_ID);
    	return String.valueOf(object);
	}
}
