package com.ecommerce.user.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecommerce.user.AppContext;
import com.ecommerce.user.AppContext.Key;
import com.ecommerce.user.service.UserSessionService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private final JwtUtil jwtUtil;

	@Autowired
	private final MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private final UserSessionService userSessionService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		log.info("jwt filter called");
		final String authHader = request.getHeader("Authorization");
		final String token;
		final String username;
		String sessionID = null;

		if (authHader == null || !authHader.startsWith("Bearer")) {
			log.info("no token found");
			filterChain.doFilter(request, response);
			return;
		}

		token = authHader.substring(7);
		username = jwtUtil.extractUsername(token);
		sessionID = jwtUtil.extractSessionId(token);
	    AppContext.set(Key.SESSION_ID, sessionID);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);

			if (jwtUtil.validateToken(token, userDetails) && userSessionService.validateSession(sessionID)) {
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						username,
						null,
						userDetails.getAuthorities()
						);
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				log.info("user authenticated!");
			}
		}
		filterChain.doFilter(request, response);
	}

}
