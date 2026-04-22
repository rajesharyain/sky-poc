package com.sky.user.application.port;

import com.sky.user.domain.model.User;

public interface CreateUserUseCase {
	User createUser(String username, String email, String password);
}
