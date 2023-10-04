//package com.ecommerce.user.security.jwt;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.ecommerce.user.exception.JwtAuthenticationException;
//import com.ecommerce.user.payloads.TokenBlaklist;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Component
//public class JwtLogoutFilter extends OncePerRequestFilter {
//
//
//	@Autowired
//	private TokenBlaklist tokenBlaklist;
//
//	@Autowired
//	private JwtAuthenticationEntryPoint jwtAuthEntryPoint;
//
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		
//		String token = request.getHeader("Authorization");
//		
//			  try {
//				  	log.info("tokens in blacklist "+tokenBlaklist);
//				if (tokenBlaklist.getTokens().contains(token)) {
//					log.info("token is blacklisted");
//					throw new JwtAuthenticationException("Token has expired");
//				}		
//			} catch (JwtAuthenticationException e) {
//				logger.error("token authentication failed "+e.getMessage());
//				jwtAuthEntryPoint.commence(request, response, e);
//				return;
//
//			}
//		filterChain.doFilter(request, response);
//		
//		
//	}
//	
//}