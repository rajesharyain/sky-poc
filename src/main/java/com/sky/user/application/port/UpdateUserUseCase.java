package com.sky.user.application.port;

import com.sky.user.domain.model.User;
import java.util.UUID;

public interface UpdateUserUseCase {

	User updateUser(UUID id, String username, String email, String password);
}
