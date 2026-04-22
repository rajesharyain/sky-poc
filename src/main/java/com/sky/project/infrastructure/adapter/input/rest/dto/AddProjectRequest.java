package com.sky.project.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddProjectRequest(
		@NotBlank(message = "name is required") @Size(min = 2, max = 80, message = "name must be between 2 and 80 characters") String name,
		@Size(max = 255, message = "description must be at most 255 characters") String description) {
}
