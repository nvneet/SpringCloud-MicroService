package com.nav.photoapp.api.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.nav.photoapp.api.users.service.IUsersService;

@Configuration
@EnableWebSecurity
public class WebSecurity {
	
	private IUsersService iUserService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private Environment env;

	@Autowired
	public WebSecurity(IUsersService iUserService, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env) {
		this.iUserService = iUserService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.env = env;
	}

	@Bean
	protected SecurityFilterChain configure (HttpSecurity httpSecurity) throws Exception  {
				
//		InetAddress localHost = InetAddress.getLocalHost();
//		System.out.println("local network hostname: " + localHost.getHostName());
//        System.out.println("local machine ip: " + localHost.getHostAddress());
//        System.out.println("env " + "hasIpAddress('" + env.getProperty("gateway.ip") + "')");

		//Configure AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder= httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        
        authenticationManagerBuilder.userDetailsService(iUserService)
        .passwordEncoder(bCryptPasswordEncoder);
        
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        
        // Adding processing url for authentication filter
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(iUserService, env, authenticationManager);
        authenticationFilter.setFilterProcessesUrl(env.getProperty("login.url.path"));
        
		httpSecurity.csrf().disable();
		
		httpSecurity.authorizeHttpRequests()
		.requestMatchers(HttpMethod.POST, "/users").permitAll()
//		.requestMatchers(HttpMethod.POST, "/users").access(
//				new WebExpressionAuthorizationManager("hasIpAddress('" + env.getProperty("gateway.ip") + "')")) // To be accessed only by gateway ip request
//		.requestMatchers("/h2-console")
		.requestMatchers(new AntPathRequestMatcher("/h2-console/*")).permitAll()
		.and()
		.addFilter(authenticationFilter)
		.authenticationManager(authenticationManager)
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		httpSecurity.headers().frameOptions().disable();
		
		return httpSecurity.build();
	}
}
