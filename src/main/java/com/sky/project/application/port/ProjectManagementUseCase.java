package com.sky.project.application.port;

import com.sky.project.domain.model.Project;
import java.util.List;
import java.util.UUID;

public interface ProjectManagementUseCase {

	Project addProjectToUser(UUID userId, String name, String description);

	List<Project> getProjectsByUserId(UUID userId);

	Project getProjectById(UUID userId, UUID projectId);

	void removeProjectFromUser(UUID userId, UUID projectId);
}
