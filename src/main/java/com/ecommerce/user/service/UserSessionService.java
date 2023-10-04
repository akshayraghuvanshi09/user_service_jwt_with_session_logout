package com.ecommerce.user.service;

import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserSession;

public interface UserSessionService {
	String saveSession(User user);

	UserSession getSession(long userId);

	boolean validateSession(String sessionId);

	void deleteSession(String sessionId);
}
