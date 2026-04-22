package com.sky.project.infrastructure.adapter.input.rest.mapper;

import com.sky.project.domain.model.Project;
import com.sky.project.infrastructure.adapter.input.rest.dto.ProjectResponse;
import org.springframework.stereotype.Component;

@Component
public class ProjectRestMapper {

	public ProjectResponse toResponse(Project project) {
		return new ProjectResponse(project.id(), project.name(), project.description());
	}
}
