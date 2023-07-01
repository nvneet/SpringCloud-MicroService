package com.nav.photoapp.api.users.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nav.photoapp.api.users.dto.UserDTO;
import com.nav.photoapp.api.users.service.IUsersService;
import com.nav.photoapp.api.users.ui.model.LoginRequestModel;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private IUsersService iUsersService;
	private Environment env;

	public AuthenticationFilter(IUsersService iUsersService,Environment env, 
			AuthenticationManager authenticationManager) {
		super(authenticationManager);
		this.iUsersService = iUsersService;
		this.env = env;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {

			LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestModel.class);

			return getAuthenticationManager().authenticate(
					new UsernamePasswordAuthenticationToken(
							creds.getEmail(), 
							creds.getPassword(), 
							new ArrayList<>()));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, 
			HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String userName = ((User)auth.getPrincipal()).getUsername();
		UserDTO user = iUsersService.getUserDetailsByEmail(userName); // user name is user's e-mail
	}

}
