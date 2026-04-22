package com.sky.project.application.service;

import com.sky.project.application.port.ProjectManagementUseCase;
import com.sky.project.domain.exception.ProjectNotFoundException;
import com.sky.project.domain.model.Project;
import com.sky.project.domain.repository.ProjectRepository;
import com.sky.user.domain.exception.UserNotFoundException;
import com.sky.user.domain.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ProjectService implements ProjectManagementUseCase {

	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
		this.projectRepository = projectRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Project addProjectToUser(UUID userId, String name, String description) {
		ensureUserExists(userId);
		return projectRepository.save(new Project(UUID.randomUUID(), userId, name, description));
	}

	@Override
	public List<Project> getProjectsByUserId(UUID userId) {
		ensureUserExists(userId);
		return projectRepository.findByUserId(userId);
	}

	@Override
	public Project getProjectById(UUID userId, UUID projectId) {
		ensureUserExists(userId);
		return projectRepository.findByIdAndUserId(projectId, userId)
				.orElseThrow(() -> new ProjectNotFoundException(userId, projectId));
	}

	@Override
	public void removeProjectFromUser(UUID userId, UUID projectId) {
		ensureUserExists(userId);
		getProjectById(userId, projectId);
		projectRepository.deleteByIdAndUserId(projectId, userId);
	}

	private void ensureUserExists(UUID userId) {
		if (userRepository.findById(userId).isEmpty()) {
			throw new UserNotFoundException(userId);
		}
	}
}
