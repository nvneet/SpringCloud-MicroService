package com.nav.apps.ws.ui.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDetailsRequestModel {
	
	@NotNull(message="firstName can not be null")
	@Size(min=3,message="first name must be of more than 3 characters")
	private String firstName;
	@NotNull(message="lastName can not be null")
	private String lastName;
	@NotNull(message="email can not be null")
	@Email
	private String email;
	@NotNull(message="password can not be null")
	@Size(min=8,max=16, message="non-compliance in allowed password length of 8-16 characters")
	private String password;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
}
