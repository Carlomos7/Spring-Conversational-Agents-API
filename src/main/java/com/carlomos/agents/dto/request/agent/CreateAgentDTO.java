package com.carlomos.agents.dto.request.agent;

import com.fasterxml.jackson.databind.JsonNode;

public record CreateAgentDTO(
                String name,
                String context,
                String firstMessage,
                JsonNode responseShape,
                String instructions) {
}
