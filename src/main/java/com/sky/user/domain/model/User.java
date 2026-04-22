package com.sky.user.domain.model;

import lombok.Getter;
import java.util.UUID;

@Getter
public class User {
	private final UUID id;
	private String username;
	private String email;
	private String password;

	public User(UUID id, String username, String email, String password) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public void updateInfo(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}
}
