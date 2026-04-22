package com.sky.user.domain.exception;

public class UsernameAlreadyTakenException extends RuntimeException {

	public UsernameAlreadyTakenException(String username) {
		super("Username already taken: " + username);
	}
}
