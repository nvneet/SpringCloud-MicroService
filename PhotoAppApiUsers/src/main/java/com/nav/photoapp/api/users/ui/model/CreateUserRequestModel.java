package com.nav.photoapp.api.users.ui.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateUserRequestModel {

	@NotNull(message="first name can not be Null")
	@Size(min=2, message="First name can not be less than two characters.")
	private String firstName;
	
	@NotNull(message="last name can not be Null")
	@Size(min=2, message="Last name can not be less than two characters.")
	private String lastName;
	
	@NotNull(message="Email can not be empty")
	@Email
	private String email;
	
	@NotNull(message="password can not be Null")
	@Size(min=8, max=16, message="Password length not matching.")
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
