package com.carlomos.agents.dto.response.agent;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

public record AgentResponseDTO(
        UUID id,
        String name,
        String context,
        String firstMessage,
        JsonNode responseShape,
        String instructions) {
}
