package com.sky.project.infrastructure.adapter.input.rest;

import com.sky.project.application.port.ProjectManagementUseCase;
import com.sky.project.infrastructure.adapter.input.rest.dto.AddProjectRequest;
import com.sky.project.infrastructure.adapter.input.rest.dto.ProjectResponse;
import com.sky.project.infrastructure.adapter.input.rest.mapper.ProjectRestMapper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/{userId}/projects")
public class UserProjectController {

	private final ProjectManagementUseCase projectManagementUseCase;
	private final ProjectRestMapper projectRestMapper;

	public UserProjectController(ProjectManagementUseCase projectManagementUseCase,
			ProjectRestMapper projectRestMapper) {
		this.projectManagementUseCase = projectManagementUseCase;
		this.projectRestMapper = projectRestMapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProjectResponse addProject(@PathVariable UUID userId, @Valid @RequestBody AddProjectRequest request) {
		return projectRestMapper
				.toResponse(projectManagementUseCase.addProjectToUser(userId, request.name(), request.description()));
	}

	@GetMapping
	public List<ProjectResponse> getProjects(@PathVariable UUID userId) {
		return projectManagementUseCase.getProjectsByUserId(userId).stream().map(projectRestMapper::toResponse)
				.toList();
	}

	@GetMapping("/{projectId}")
	public ProjectResponse getProject(@PathVariable UUID userId, @PathVariable UUID projectId) {
		return projectRestMapper.toResponse(projectManagementUseCase.getProjectById(userId, projectId));
	}

	@DeleteMapping("/{projectId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeProject(@PathVariable UUID userId, @PathVariable UUID projectId) {
		projectManagementUseCase.removeProjectFromUser(userId, projectId);
	}
}
