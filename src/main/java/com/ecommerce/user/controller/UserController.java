package com.ecommerce.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.user.dto.UserDTO;
import com.ecommerce.user.payloads.ResponseHandler;
import com.ecommerce.user.service.UserService;

@RequestMapping("/api/v1/user")
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/save")
	public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){
		UserDTO saveUser = userService.saveUser(userDTO);
		return ResponseHandler.responseBuilder("user created successfully!", HttpStatus.CREATED, saveUser);
	}

	@GetMapping("/get-all")
	@PreAuthorize("hasAuthority('CUSTOMER')")
	public ResponseEntity<?> getAllUser(){
		List<UserDTO> allUser = userService.getAllUser();
		return ResponseHandler.responseBuilder("all users", HttpStatus.OK, allUser);
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getUserById(@PathVariable long id){
		UserDTO user = userService.getUserById(id);
		return ResponseHandler.responseBuilder("user with id: "+id, HttpStatus.OK, user);
	}
}
