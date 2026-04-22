package com.sky.user.application.port;

import com.sky.user.domain.model.User;
import java.util.UUID;

public interface GetUserUseCase {
	User getUserById(UUID id);
	User getUserByUsername(String username);
}
