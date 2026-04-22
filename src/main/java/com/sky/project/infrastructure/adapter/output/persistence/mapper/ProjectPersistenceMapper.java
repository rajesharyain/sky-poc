package com.sky.project.infrastructure.adapter.output.persistence.mapper;

import com.sky.project.domain.model.Project;
import com.sky.project.infrastructure.adapter.output.persistence.entity.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectPersistenceMapper {

	public ProjectEntity toEntity(Project project) {
		ProjectEntity entity = new ProjectEntity();
		entity.setId(project.id());
		entity.setUserId(project.userId());
		entity.setName(project.name());
		entity.setDescription(project.description());
		return entity;
	}

	public Project toDomain(ProjectEntity entity) {
		return new Project(entity.getId(), entity.getUserId(), entity.getName(), entity.getDescription());
	}
}
