package com.nav.photoapp.api.users.security;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
	// attemptAuthentication method from UsernamePasswordAuthenticationFilter superclass
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
	// successfulAuthentication method from UsernamePasswordAuthenticationFilter <- AbstractAuthenticationProcessingFilter superclass
	protected void successfulAuthentication(HttpServletRequest req, 
			HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String userName = ((User)auth.getPrincipal()).getUsername();
		UserDTO user = iUsersService.getUserDetailsByEmail(userName); // user name is user's e-mail

		Instant now = Instant.now();
		
		// reading values from property file
		String token_expiry = env.getProperty("token.expiration_time");
		String token_secret = env.getProperty("token.secret");
		
		// For signing key for jwt access token
		byte[] secretKeyBytes = Base64.getEncoder().encode(token_secret.getBytes());
		SecretKey secretKet = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
		
		// Create JWT token
		String token = Jwts.builder().setSubject(user.getUserId())
		.setExpiration(Date.from(now.plusMillis(Long.parseLong(token_expiry))))
		.setIssuedAt(Date.from(now))
		.signWith(secretKet, SignatureAlgorithm.HS512)
		.compact();
		
		// now add the created jwt token to HttpServletResponse header and returning back to client app
		res.addHeader("token", token);
		res.addHeader("userId", user.getUserId());
	}

}
