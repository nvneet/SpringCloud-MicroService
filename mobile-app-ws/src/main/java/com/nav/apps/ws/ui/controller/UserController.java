package com.nav.apps.ws.ui.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.nav.apps.ws.exceptions.UserServiceException;
import com.nav.apps.ws.ui.model.request.UpdateUserDetailsRequestModel;
import com.nav.apps.ws.ui.model.request.UserDetailsRequestModel;
import com.nav.apps.ws.ui.model.response.UserRest;
import com.nav.apps.ws.userservice.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {
	
	@Autowired
	IUserService userService;
	
	Map<String, UserRest> usersMap = new HashMap<>();
	
	//=>  http://localhost:8080/users?page=2&limit=50
	//=>  http://localhost:8080/users?page=2
	//=>  http://localhost:8080/users?page=2&limit=50  
	//=> test using Optional parameter "int sort" and "String sort" with required false.
	// when we dont pass sort parameter in request then, int sort gives error, while String sort doesn't, why ?
	@GetMapping
	public String getUsers(@RequestParam(value="page", defaultValue="1")int page,         
						   @RequestParam(value="limit", defaultValue="40") int limit,         
						   @RequestParam(value="sort", required=false) String sort) {
		return "GET user method is called for users with page = " + page + "  limit = " + limit;
	}
	
//	@GetMapping(path="/{userId}", produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
//	public UserRest getUser(@PathVariable String userId) {
//		UserRest userRest = getSampleUser();
//		return userRest;
//	}
	
//  To add custom http response
//	@GetMapping(path="/{userId}", produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
//	public ResponseEntity<UserRest> getUser(@PathVariable String userId) {
//		UserRest userRest = getSampleUser();
//		return new ResponseEntity<UserRest>(HttpStatus.BAD_REQUEST);
//	}
	
	@GetMapping(path="/{userId}", produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserRest> getUser(@PathVariable String userId) {
		
		// Alien code to test exception handling, delete this code block once tested.
		// if (true) throw new UserServiceException("User Service Exception is thrown.");
		// Alien code ended. 
		
		if(usersMap.containsKey(userId))
			return new ResponseEntity<>(usersMap.get(userId), HttpStatus.OK); //return new ResponseEntity<UserRest>
		else return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // type inference
	}
	
	@PostMapping(consumes= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			     produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserDetailsRequestModel userDetails) {

		UserRest userRest = userService.createUser(userDetails);
		return new ResponseEntity<UserRest>(userRest, HttpStatus.OK);
	}
	
	@PutMapping(path="/{userId}", consumes= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
		     produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest updateUser(@PathVariable String userId, @Valid @RequestBody UpdateUserDetailsRequestModel updateUserDetails) {
		
		UserRest userRest = usersMap.get(userId);
		userRest.setFirstName(updateUserDetails.getFirstName());
		userRest.setLastName(updateUserDetails.getLastName());
		
		//update users map
		usersMap.put(userId, userRest);
		
		return userRest;
	}
	
	@DeleteMapping(path="/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable String id) {
		
		usersMap.remove(id);
		
		return ResponseEntity.noContent().build();
	}
	
	private UserRest getSampleUser() {
		UserRest userRest = new UserRest();
		userRest.setFirstName("Navneet");
		userRest.setLastName("Mishra");
		userRest.setEmail("nk@iitd.ac.in");
		return userRest;
	}

}
