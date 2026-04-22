package com.sky.project.domain.repository;

import com.sky.project.domain.model.Project;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository {

	Project save(Project project);

	List<Project> findByUserId(UUID userId);

	Optional<Project> findByIdAndUserId(UUID id, UUID userId);

	void deleteByIdAndUserId(UUID id, UUID userId);
}
