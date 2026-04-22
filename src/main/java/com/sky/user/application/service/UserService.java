package com.sky.user.application.service;

import com.sky.user.application.port.CreateUserUseCase;
import com.sky.user.application.port.GetUserUseCase;
import com.sky.user.application.port.UpdateUserUseCase;
import com.sky.user.domain.exception.UserNotFoundException;
import com.sky.user.domain.exception.UsernameAlreadyTakenException;
import com.sky.user.domain.model.User;
import com.sky.user.domain.repository.UserRepository;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService implements CreateUserUseCase, GetUserUseCase, UpdateUserUseCase {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User createUser(String username, String email, String password) {
		if (userRepository.findByUsername(username).isPresent()) {
			throw new UsernameAlreadyTakenException(username);
		}
		User user = new User(UUID.randomUUID(), username, email, passwordEncoder.encode(password));
		return userRepository.save(user);
	}

	@Override
	public User getUserById(UUID id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
	}

	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
	}

	@Override
	public User updateUser(UUID id, String username, String email, String password) {
		User user = getUserById(id);

		if (!user.getUsername().equals(username) && userRepository.findByUsername(username).isPresent()) {
			throw new UsernameAlreadyTakenException(username);
		}

		user.updateInfo(username, email, passwordEncoder.encode(password));
		return userRepository.save(user);
	}
}
