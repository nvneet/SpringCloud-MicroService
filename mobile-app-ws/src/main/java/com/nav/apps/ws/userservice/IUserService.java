package com.nav.apps.ws.userservice;

import com.nav.apps.ws.ui.model.request.UserDetailsRequestModel;
import com.nav.apps.ws.ui.model.response.UserRest;

public interface IUserService {
	
	UserRest createUser(UserDetailsRequestModel userDetails);

}
