package com.nav.apps.ws.utils;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class UserUtil {

	public String generateUserId() {
		return UUID.randomUUID().toString();
	}
}
