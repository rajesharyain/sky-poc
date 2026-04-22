package com.sky.user.domain.repository;

import com.sky.user.domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
	User save(User user);
	Optional<User> findById(UUID id);
	Optional<User> findByUsername(String username);
	void deleteById(UUID id);
}
