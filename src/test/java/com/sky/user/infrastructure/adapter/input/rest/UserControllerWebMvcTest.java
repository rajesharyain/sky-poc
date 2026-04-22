package com.sky.user.infrastructure.adapter.input.rest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.user.application.port.CreateUserUseCase;
import com.sky.user.application.port.GetUserUseCase;
import com.sky.user.application.port.UpdateUserUseCase;
import com.sky.user.domain.model.User;
import com.sky.user.infrastructure.adapter.input.rest.dto.CreateUserRequest;
import com.sky.user.infrastructure.adapter.input.rest.dto.UpdateUserRequest;
import com.sky.user.infrastructure.adapter.input.rest.error.RestExceptionHandler;
import com.sky.user.infrastructure.adapter.input.rest.mapper.UserRestMapper;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@Import({UserRestMapper.class, RestExceptionHandler.class, com.sky.user.infrastructure.config.SecurityConfig.class})
class UserControllerWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CreateUserUseCase createUserUseCase;

	@MockBean
	private GetUserUseCase getUserUseCase;

	@MockBean
	private UpdateUserUseCase updateUserUseCase;

	@Test
	void createUser_returns201() throws Exception {
		UUID id = UUID.randomUUID();
		when(createUserUseCase.createUser(eq("alice"), eq("a@b.c"), eq("password123")))
				.thenReturn(new User(id, "alice", "a@b.c", "enc"));

		mockMvc.perform(post("/api/v1/users").with(httpBasic("api-admin", "api-admin-password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new CreateUserRequest("alice", "a@b.c", "password123"))))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(id.toString()))
				.andExpect(jsonPath("$.username").value("alice")).andExpect(jsonPath("$.email").value("a@b.c"));
	}

	@Test
	void getUserById_returns200() throws Exception {
		UUID id = UUID.randomUUID();
		when(getUserUseCase.getUserById(id)).thenReturn(new User(id, "alice", "a@b.c", "enc"));

		mockMvc.perform(get("/api/v1/users/{id}", id)).andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("alice"));
	}

	@Test
	void getUserByUsername_returns200() throws Exception {
		UUID id = UUID.randomUUID();
		when(getUserUseCase.getUserByUsername("alice")).thenReturn(new User(id, "alice", "a@b.c", "enc"));

		mockMvc.perform(get("/api/v1/users/username/{username}", "alice")).andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("a@b.c"));
	}

	@Test
	void updateUser_returns200() throws Exception {
		UUID id = UUID.randomUUID();
		when(updateUserUseCase.updateUser(eq(id), eq("alice2"), eq("a2@b.c"), eq("password234")))
				.thenReturn(new User(id, "alice2", "a2@b.c", "enc"));

		mockMvc.perform(put("/api/v1/users/{id}", id).with(httpBasic("api-admin", "api-admin-password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new UpdateUserRequest("alice2", "a2@b.c", "password234"))))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id.toString()))
				.andExpect(jsonPath("$.username").value("alice2")).andExpect(jsonPath("$.email").value("a2@b.c"));
	}

	@Test
	void createUser_invalidBody_returns400WithFieldErrors() throws Exception {
		mockMvc.perform(post("/api/v1/users").with(httpBasic("api-admin", "api-admin-password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new CreateUserRequest("", "bad-email", "123"))))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Validation failed"))
				.andExpect(jsonPath("$.errors.username").exists()).andExpect(jsonPath("$.errors.email").exists())
				.andExpect(jsonPath("$.errors.password").exists());
	}
}
