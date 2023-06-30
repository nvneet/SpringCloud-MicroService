package com.nav.photoapp.api.users.security;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity {
	
	private Environment env;
	
	@Autowired
	public WebSecurity(Environment env) {
		this.env = env;
	}

	@Bean
	protected SecurityFilterChain configure (HttpSecurity httpSecurity) throws Exception  {
		
		InetAddress localHost = InetAddress.getLocalHost();
		System.out.println("local network hostname: " + localHost.getHostName());
        System.out.println("local machine ip: " + localHost.getHostAddress());
        System.out.println("env " + "hasIpAddress('" + env.getProperty("gateway.ip") + "')");

        AuthenticationManagerBuilder authenticationManagerBuilder= httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        
		httpSecurity.csrf().disable();
		
		httpSecurity.authorizeHttpRequests()
//		.requestMatchers(HttpMethod.POST, "/users").permitAll()
		.requestMatchers(HttpMethod.POST, "/users").access(
				new WebExpressionAuthorizationManager("hasIpAddress('" + env.getProperty("gateway.ip") + "')")) // To be accessed only by gateway ip request
//		.requestMatchers("/h2-console")
		.requestMatchers(new AntPathRequestMatcher("/h2-console/*")).permitAll()
		.and()
		.addFilter(new AuthenticationFilter(authenticationManager))
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		httpSecurity.headers().frameOptions().disable();
		
		return httpSecurity.build();
	}
}
