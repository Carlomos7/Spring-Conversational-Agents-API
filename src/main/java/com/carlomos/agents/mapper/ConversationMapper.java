package com.carlomos.agents.mapper;

import java.util.List;

import com.carlomos.agents.dto.response.conversation.ConversationResponse;
import com.carlomos.agents.entity.Conversation;

public final class ConversationMapper {

    private ConversationMapper() {
    }

    public static ConversationResponse toResponse(Conversation entity) {
        return new ConversationResponse(
                entity.getId(),
                entity.getAgent().getId(),
                entity.getCreatedAt());
    }

    public static List<ConversationResponse> toResponseList(List<Conversation> list) {
        return list.stream().map(ConversationMapper::toResponse).toList();
    }
}
