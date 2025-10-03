package com.carlomos.agents.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carlomos.agents.entity.Agent;
import com.carlomos.agents.entity.Conversation;
import com.carlomos.agents.repository.AgentRepository;
import com.carlomos.agents.repository.ConversationRepository;

@Service
@Transactional
public class ConversationService { // TODO: Add custom exceptions
    private final ConversationRepository conversationRepository;
    private final AgentRepository agentRepository;

    public ConversationService(ConversationRepository conversationRepository, AgentRepository agentRepository) {
        this.conversationRepository = conversationRepository;
        this.agentRepository = agentRepository;
    }

    @Transactional(readOnly = true)
    public Conversation findById(java.util.UUID id) {
        return conversationRepository.findById(id).orElse(null);
    }

    public Conversation createForAgent(UUID agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found"));
        Conversation conversation = new Conversation(agent);
        return conversationRepository.save(conversation);
    }

    @Transactional(readOnly = true)
    public List<Conversation> listByAgent(UUID agentId, Pageable pageable) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found"));
        return conversationRepository.findAgentOrderByCreatedAtDesc(agent, pageable);
    }

    public void deleteById(UUID id) {
        conversationRepository.deleteById(id);
    }
}
