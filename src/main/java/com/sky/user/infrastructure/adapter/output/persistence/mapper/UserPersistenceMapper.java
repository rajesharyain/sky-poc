package com.sky.user.infrastructure.adapter.output.persistence.mapper;

import com.sky.user.domain.model.User;
import com.sky.user.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

	public UserEntity toEntity(User user) {
		UserEntity entity = new UserEntity();
		entity.setId(user.getId());
		entity.setUsername(user.getUsername());
		entity.setEmail(user.getEmail());
		entity.setPassword(user.getPassword());
		return entity;
	}

	public User toDomain(UserEntity entity) {
		return new User(entity.getId(), entity.getUsername(), entity.getEmail(), entity.getPassword());
	}
}
