package com.sky.user.infrastructure.config;

import com.sky.user.application.port.CreateUserUseCase;
import com.sky.user.application.port.GetUserUseCase;
import com.sky.user.application.port.UpdateUserUseCase;
import com.sky.user.application.service.UserService;
import com.sky.user.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

	@Bean
	public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return new UserService(userRepository, passwordEncoder);
	}

	@Bean
	public CreateUserUseCase createUserUseCase(UserService userService) {
		return userService;
	}

	@Bean
	public GetUserUseCase getUserUseCase(UserService userService) {
		return userService;
	}

	@Bean
	public UpdateUserUseCase updateUserUseCase(UserService userService) {
		return userService;
	}
}
