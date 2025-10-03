package com.carlomos.agents.dto.response.message;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID conversationId,
        Instant createdAt,
        String role,
        String content) {

}
