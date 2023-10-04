package com.ecommerce.user.security.jwt;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.user.AppContext;
import com.ecommerce.user.AppContext.Key;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.UserNotFoundException;
import com.ecommerce.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = Optional.ofNullable(userRepository.findByEmail(username)
				.orElseThrow(() -> new UserNotFoundException("user doest not exist with username : " + username)));
		user.ifPresent(t -> {
			AppContext.set(Key.USER_ID, String.valueOf(t.getId()));
			AppContext.set(Key.USER_EMAIL, t.getEmail());		
		});
		return user.orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
	}

}
