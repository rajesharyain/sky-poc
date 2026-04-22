package com.sky.project.infrastructure.adapter.output.persistence;

import com.sky.project.domain.model.Project;
import com.sky.project.domain.repository.ProjectRepository;
import com.sky.project.infrastructure.adapter.output.persistence.mapper.ProjectPersistenceMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProjectPersistenceAdapter implements ProjectRepository {

	private final JpaProjectRepository jpaProjectRepository;
	private final ProjectPersistenceMapper mapper;

	public ProjectPersistenceAdapter(JpaProjectRepository jpaProjectRepository, ProjectPersistenceMapper mapper) {
		this.jpaProjectRepository = jpaProjectRepository;
		this.mapper = mapper;
	}

	@Override
	@Transactional
	public Project save(Project project) {
		return mapper.toDomain(jpaProjectRepository.save(mapper.toEntity(project)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Project> findByUserId(UUID userId) {
		return jpaProjectRepository.findByUserId(userId).stream().map(mapper::toDomain).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Project> findByIdAndUserId(UUID id, UUID userId) {
		return jpaProjectRepository.findByIdAndUserId(id, userId).map(mapper::toDomain);
	}

	@Override
	@Transactional
	public void deleteByIdAndUserId(UUID id, UUID userId) {
		jpaProjectRepository.deleteByIdAndUserId(id, userId);
	}
}
