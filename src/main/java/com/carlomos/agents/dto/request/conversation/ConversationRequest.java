package com.carlomos.agents.dto.request.conversation;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record ConversationRequest(
        @NotNull UUID agentId) {
}
