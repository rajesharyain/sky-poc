package com.sky.project.infrastructure.adapter.output.persistence;

import com.sky.project.infrastructure.adapter.output.persistence.entity.ProjectEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, UUID> {

	List<ProjectEntity> findByUserId(UUID userId);

	Optional<ProjectEntity> findByIdAndUserId(UUID id, UUID userId);

	void deleteByIdAndUserId(UUID id, UUID userId);
}
