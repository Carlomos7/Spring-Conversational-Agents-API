package com.carlomos.agents.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carlomos.agents.entity.Conversation;
import com.carlomos.agents.entity.Message;
import com.carlomos.agents.repository.MessageRepository;

@Service
@Transactional
public class MessageService {
// TODO: Add custom exceptions
    private static final Set<String> ALLOWED_ROLES = Set.of("user", "agent", "system");

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;

    public MessageService(MessageRepository messageRepository, ConversationService conversationService) {
        this.messageRepository = messageRepository;
        this.conversationService = conversationService;
    }

    @Transactional(readOnly = true)
    public Message findById(UUID id) {
        return messageRepository.findById(id).orElse(null);
    }

    public Message append(UUID conversationId, String role, String content) {
        if (!ALLOWED_ROLES.contains(role)) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        var conversation = conversationService.findById(conversationId);
        if (conversation == null) {
            throw new IllegalArgumentException("Conversation not found");
        }
        var message = new Message(conversation, role, content);
        return messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public List<Message> listByConversation(UUID conversationId, Pageable pageable) {
        Conversation conversation = conversationService.findById(conversationId);
        if (conversation == null) {
            throw new IllegalArgumentException("Conversation not found");
        }
        return messageRepository.findByConversationOrderByCreatedAtAsc(conversationId, pageable);
    }

    public void deleteById(UUID id) {
        messageRepository.deleteById(id);
    }
}
