package com.sky.project.infrastructure.adapter.input.rest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.project.application.port.ProjectManagementUseCase;
import com.sky.project.domain.model.Project;
import com.sky.project.infrastructure.adapter.input.rest.dto.AddProjectRequest;
import com.sky.project.infrastructure.adapter.input.rest.mapper.ProjectRestMapper;
import com.sky.user.infrastructure.adapter.input.rest.error.RestExceptionHandler;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserProjectController.class)
@Import({ProjectRestMapper.class, RestExceptionHandler.class,
		com.sky.user.infrastructure.config.SecurityConfig.class})
class UserProjectControllerWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProjectManagementUseCase projectManagementUseCase;

	@Test
	void addProject_returns201() throws Exception {
		UUID userId = UUID.randomUUID();
		UUID projectId = UUID.randomUUID();
		when(projectManagementUseCase.addProjectToUser(eq(userId), eq("news"), eq("daily digest")))
				.thenReturn(new Project(projectId, userId, "news", "daily digest"));

		mockMvc.perform(post("/api/v1/users/{userId}/projects", userId).with(httpBasic("api-admin", "api-admin-password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new AddProjectRequest("news", "daily digest"))))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(projectId.toString()))
				.andExpect(jsonPath("$.name").value("news"));
	}

	@Test
	void getProjects_returns200() throws Exception {
		UUID userId = UUID.randomUUID();
		UUID projectId = UUID.randomUUID();
		when(projectManagementUseCase.getProjectsByUserId(userId))
				.thenReturn(java.util.List.of(new Project(projectId, userId, "news", "daily digest")));

		mockMvc.perform(get("/api/v1/users/{userId}/projects", userId)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(projectId.toString()))
				.andExpect(jsonPath("$[0].name").value("news"));
	}

	@Test
	void getProject_returns200() throws Exception {
		UUID userId = UUID.randomUUID();
		UUID projectId = UUID.randomUUID();
		when(projectManagementUseCase.getProjectById(userId, projectId))
				.thenReturn(new Project(projectId, userId, "news", "daily digest"));

		mockMvc.perform(get("/api/v1/users/{userId}/projects/{projectId}", userId, projectId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(projectId.toString())).andExpect(jsonPath("$.name").value("news"));
	}

	@Test
	void addProject_invalidBody_returns400() throws Exception {
		UUID userId = UUID.randomUUID();

		mockMvc.perform(post("/api/v1/users/{userId}/projects", userId).with(httpBasic("api-admin", "api-admin-password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new AddProjectRequest("", "d".repeat(260)))))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Validation failed"))
				.andExpect(jsonPath("$.errors.name").exists()).andExpect(jsonPath("$.errors.description").exists());
	}

	@Test
	void removeProject_returns204() throws Exception {
		UUID userId = UUID.randomUUID();
		UUID projectId = UUID.randomUUID();
		doNothing().when(projectManagementUseCase).removeProjectFromUser(userId, projectId);

		mockMvc.perform(delete("/api/v1/users/{userId}/projects/{projectId}", userId, projectId)
				.with(httpBasic("api-admin", "api-admin-password")))
				.andExpect(status().isNoContent());
	}
}
