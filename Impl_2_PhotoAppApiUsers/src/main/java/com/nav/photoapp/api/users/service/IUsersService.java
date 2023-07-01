package com.nav.photoapp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.nav.photoapp.api.users.dto.UserDTO;

public interface IUsersService extends UserDetailsService{

	UserDTO createUser (UserDTO userDetails);
	UserDTO getUserDetailsByEmail(String email);
}
