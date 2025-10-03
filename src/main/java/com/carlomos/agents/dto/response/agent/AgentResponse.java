package com.carlomos.agents.dto.response.agent;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

public record AgentResponse(
                UUID id,
                Instant createdAt,
                String name,
                String context,
                String firstMessage,
                JsonNode responseShape,
                String instructions) {
}
