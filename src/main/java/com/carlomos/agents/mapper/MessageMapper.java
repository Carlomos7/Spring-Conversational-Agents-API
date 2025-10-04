package com.carlomos.agents.mapper;

import java.util.List;

import com.carlomos.agents.dto.response.message.MessageResponse;
import com.carlomos.agents.entity.Message;

public final class MessageMapper {
    private MessageMapper() {
    }

    public static MessageResponse toResponse(Message entity) {
        return new MessageResponse(
                entity.getId(),
                entity.getConversation() != null ? entity.getConversation().getId() : null,
                entity.getCreatedAt(),
                entity.getRole(),
                entity.getContent());
    }

    public static List<MessageResponse> toResponseList(List<Message> list) {
        return list.stream().map(MessageMapper::toResponse).toList();
    }
}
