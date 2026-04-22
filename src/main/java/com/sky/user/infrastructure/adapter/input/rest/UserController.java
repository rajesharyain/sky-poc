package com.sky.user.infrastructure.adapter.input.rest;

import com.sky.user.application.port.CreateUserUseCase;
import com.sky.user.application.port.GetUserUseCase;
import com.sky.user.application.port.UpdateUserUseCase;
import com.sky.user.domain.model.User;
import com.sky.user.infrastructure.adapter.input.rest.dto.CreateUserRequest;
import com.sky.user.infrastructure.adapter.input.rest.dto.UpdateUserRequest;
import com.sky.user.infrastructure.adapter.input.rest.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final CreateUserUseCase createUserUseCase;
	private final GetUserUseCase getUserUseCase;
	private final UpdateUserUseCase updateUserUseCase;

	public UserController(CreateUserUseCase createUserUseCase, GetUserUseCase getUserUseCase,
			UpdateUserUseCase updateUserUseCase) {
		this.createUserUseCase = createUserUseCase;
		this.getUserUseCase = getUserUseCase;
		this.updateUserUseCase = updateUserUseCase;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
		User user = createUserUseCase.createUser(request.username(), request.email(), request.password());
		return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
	}

	@GetMapping("/{id}")
	public UserResponse getUserById(@PathVariable UUID id) {
		User user = getUserUseCase.getUserById(id);
		return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
	}

	@GetMapping("/username/{username}")
	public UserResponse getUserByUsername(@PathVariable String username) {
		User user = getUserUseCase.getUserByUsername(username);
		return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
	}

	@PutMapping("/{id}")
	public UserResponse updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
		User user = updateUserUseCase.updateUser(id, request.username(), request.email(), request.password());
		return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
	}
}
