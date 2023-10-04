package com.ecommerce.user.service;

import java.util.List;

import com.ecommerce.user.dto.UserDTO;

public interface UserService {

	UserDTO saveUser(UserDTO userDTO);
	
	List<UserDTO> getAllUser();
	
	UserDTO getUserById(Long id);
	
}
