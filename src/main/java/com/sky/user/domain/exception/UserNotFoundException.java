package com.sky.user.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException(UUID id) {
		super("User not found: id=" + id);
	}

	public UserNotFoundException(String username) {
		super("User not found: username=" + username);
	}
}
