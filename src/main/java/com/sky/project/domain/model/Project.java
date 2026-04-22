package com.sky.project.domain.model;

import java.util.UUID;

public record Project(UUID id, UUID userId, String name, String description) {
}
