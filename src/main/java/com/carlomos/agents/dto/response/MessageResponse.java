package com.carlomos.agents.dto.response;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID conversationId,
        Instant createdAt,
        String role,
        String content) {

}
