package com.ecommerce.user.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserSession;
import com.ecommerce.user.repository.UserSessionRepository;
import com.ecommerce.user.service.UserSessionService;


@Service
public class UserSessionServiceImpl implements UserSessionService {
	@Value("${session.max.inactivity:5}")
	private int maxInactivity;
	
	@Value("${session.max.inactivity:10}")
	private int maxValidity;
	
	@Autowired
	private UserSessionRepository userSessionRepository;

	@Override
	public String saveSession(User user) {
		try {
			System.out.println("deleteByUserId is called..");
			userSessionRepository.deleteByUserId(user.getId());
		}catch(Exception e) {
			
		}
		UserSession session = UserSession.builder().id(UUID.randomUUID().toString()).lastAccessed(LocalDateTime.now()).loginTme(LocalDateTime.now())
				.userId(user.getId()).userName(user.getFname()).build();
		return userSessionRepository.save(session).getId();
	}

	@Override
	public UserSession getSession(long userId) {
		return userSessionRepository.findByUserId(userId).orElse(null);
	}

	@Override
	public boolean validateSession(String sessionId) {
		boolean isValidated = false;
		UserSession session = userSessionRepository.findById(sessionId).orElse(null);
		if(session!=null) {
		LocalDateTime lastAccessed = session.getLastAccessed();
		LocalDateTime now = LocalDateTime.now();
		long between = ChronoUnit.MINUTES.between(lastAccessed, now);
		if(between<maxInactivity) {
			isValidated = true;
			session.setLastAccessed(now);
			userSessionRepository.save(session);
		}
		}
		return isValidated;
	}

	@Override
	public void deleteSession(String sessionId) {
		userSessionRepository.deleteById(sessionId);	
	}

}
