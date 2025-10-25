package com.carlomos.agents.dto.request;

import com.fasterxml.jackson.databind.JsonNode;

public record AgentRequest(
                String name,
                String context,
                String firstMessage,
                JsonNode responseShape,
                String instructions) {
}
