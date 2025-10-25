package com.carlomos.agents.dto.response;

import java.time.Instant;
import java.util.UUID;

public record ConversationResponse(
        UUID id,
        UUID agentId,
        Instant createdAt) {

}
