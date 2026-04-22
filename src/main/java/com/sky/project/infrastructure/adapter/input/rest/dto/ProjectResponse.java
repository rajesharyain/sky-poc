package com.sky.project.infrastructure.adapter.input.rest.dto;

import java.util.UUID;

public record ProjectResponse(UUID id, String name, String description) {
}
