package com.nav.apps.ws.userservice.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nav.apps.ws.ui.model.request.UserDetailsRequestModel;
import com.nav.apps.ws.ui.model.response.UserRest;
import com.nav.apps.ws.userservice.IUserService;
import com.nav.apps.ws.utils.UserUtil;

@Service
public class UserServiceImpl implements IUserService {

	public static Map<String, UserRest> usersMap;
	UserUtil userUtil;
	
	public UserServiceImpl () {};
	
	@Autowired
	public UserServiceImpl (UserUtil userUtil) {
		this.userUtil = userUtil;
	}
	
	@Override
	public UserRest createUser(UserDetailsRequestModel userDetails) {

		UserRest userRest = new UserRest();
		userRest.setFirstName(userDetails.getFirstName());
		userRest.setLastName(userDetails.getLastName());
		userRest.setEmail(userDetails.getEmail());
		
//		String userId = UUID.randomUUID().toString();
		String userId = userUtil.generateUserId();
		userRest.setUserId(userId);		
		
		if (usersMap == null) usersMap = new HashMap<>();
		usersMap.put(userId, userRest);
		
		return userRest;
	}

}
