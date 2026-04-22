package com.sky;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.project.infrastructure.adapter.input.rest.dto.AddProjectRequest;
import com.sky.user.infrastructure.adapter.input.rest.dto.CreateUserRequest;
import com.sky.user.infrastructure.adapter.input.rest.dto.UpdateUserRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class SkyPocUserApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void userAndProjectEndpoints() throws Exception {
		MvcResult created = mockMvc
				.perform(post("/api/v1/users").with(httpBasic("api-admin", "api-admin-password"))
						.contentType(MediaType.APPLICATION_JSON).content(
						objectMapper.writeValueAsString(new CreateUserRequest("api-user", "api@test.dev", "secret12"))))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.username").value("api-user")).andReturn();

		String idStr = objectMapper.readTree(created.getResponse().getContentAsString()).get("id").asText();
		UUID userId = UUID.fromString(idStr);

		mockMvc.perform(get("/api/v1/users/{id}", userId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("api@test.dev"));

		mockMvc.perform(get("/api/v1/users/username/{username}", "api-user")).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(idStr));

		MvcResult projectResult = mockMvc
				.perform(post("/api/v1/users/{userId}/projects", userId).with(httpBasic("api-admin", "api-admin-password"))
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new AddProjectRequest("News", "Daily"))))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.name").value("News")).andReturn();

		String projectIdStr = objectMapper.readTree(projectResult.getResponse().getContentAsString()).get("id")
				.asText();

		mockMvc.perform(get("/api/v1/users/{userId}/projects", userId)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(projectIdStr));

		mockMvc.perform(get("/api/v1/users/{userId}/projects/{projectId}", userId, projectIdStr))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("News"));

		mockMvc.perform(put("/api/v1/users/{id}", userId).with(httpBasic("api-admin", "api-admin-password"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
						new UpdateUserRequest("api-user-updated", "updated@test.dev", "newpass12"))))
				.andExpect(status().isOk()).andExpect(jsonPath("$.username").value("api-user-updated"))
				.andExpect(jsonPath("$.email").value("updated@test.dev"));

		mockMvc.perform(delete("/api/v1/users/{userId}/projects/{projectId}", userId, projectIdStr)
				.with(httpBasic("api-admin", "api-admin-password")))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/v1/users/{id}", UUID.randomUUID())).andExpect(status().isNotFound());
	}

	@Test
	void createDuplicateUsername_returns409() throws Exception {
		var body = objectMapper.writeValueAsString(new CreateUserRequest("dup-user", "a@test.dev", "secret12"));
		mockMvc.perform(post("/api/v1/users").with(httpBasic("api-admin", "api-admin-password"))
				.contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isCreated());

		mockMvc.perform(post("/api/v1/users").with(httpBasic("api-admin", "api-admin-password"))
				.contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Username already taken"));
	}
}
