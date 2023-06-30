package com.nav.photoapp.api.users.ui.controllers;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nav.photoapp.api.users.dto.UserDTO;
import com.nav.photoapp.api.users.service.IUsersService;
import com.nav.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.nav.photoapp.api.users.ui.model.CreateUserResponseModel;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private Environment env;
	
	@Autowired
	IUsersService usersService;
	
	@GetMapping("/status/check")
	public String status() {
		return "Working on port " + env.getProperty("local.server.port");
	}

	@PostMapping
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel user) {
//	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequestModel user) {
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		
		UserDTO createdUser = usersService.createUser(userDTO);
		 CreateUserResponseModel createdUserData = modelMapper.map(createdUser, CreateUserResponseModel.class);
		
//		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.CREATED);
		 return ResponseEntity.status(HttpStatus.CREATED).body(createdUserData);
	}
}
