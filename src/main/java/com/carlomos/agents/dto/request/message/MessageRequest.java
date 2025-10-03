package com.carlomos.agents.dto.request.message;

import jakarta.validation.constraints.NotBlank;

public record MessageRequest(
        // TODO: Add role validation
        String role,
        @NotBlank String content) {
}
