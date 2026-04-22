package com.sky.user.infrastructure.adapter.input.rest.mapper;

import com.sky.user.domain.model.User;
import com.sky.user.infrastructure.adapter.input.rest.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserRestMapper {

	public UserResponse toResponse(User user) {
		return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
	}
}
