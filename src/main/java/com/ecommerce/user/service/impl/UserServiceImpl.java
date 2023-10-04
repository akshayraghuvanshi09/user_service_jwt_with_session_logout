package com.ecommerce.user.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.user.dto.UserDTO;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.entity.UserRole;
import com.ecommerce.user.exception.UserAlreadyExistException;
import com.ecommerce.user.exception.UserNotFoundException;
import com.ecommerce.user.repository.RoleRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDTO saveUser(UserDTO userDTO) {
		Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
		if (userOptional.isPresent()) {
			throw new UserAlreadyExistException("user wih email : " + userDTO.getEmail() + " already exist");
		}
		User user = modelMapper.map(userDTO, User.class);
		final Integer role_id   = user.getRole().getRid();
		UserRole userRole = roleRepository.findById(role_id)
				.orElseThrow(() -> new RuntimeException("role not found with id :"+role_id ));
		user.setRole(userRole);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = userRepository.save(user);
		return modelMapper.map(user, UserDTO.class);
	}

	@Override
	public List<UserDTO> getAllUser() {
		List<User> all = userRepository.findAll();
		return Arrays.asList(modelMapper.map(all, UserDTO[].class));
	}

	@Override
	public UserDTO getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("user with id : " + id + " not found"));
		return modelMapper.map(user, UserDTO.class);
	}

}
