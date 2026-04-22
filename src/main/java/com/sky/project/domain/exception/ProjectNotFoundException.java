package com.sky.project.domain.exception;

import java.util.UUID;

public class ProjectNotFoundException extends RuntimeException {

	public ProjectNotFoundException(UUID userId, UUID projectId) {
		super("Project not found: userId=" + userId + ", projectId=" + projectId);
	}
}
