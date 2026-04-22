package com.sky.user.infrastructure.adapter.input.rest.dto;

import java.util.UUID;

public record UserResponse(UUID id, String username, String email) {
}
