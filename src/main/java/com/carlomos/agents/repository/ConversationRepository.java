package com.carlomos.agents.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carlomos.agents.entity.Agent;
import com.carlomos.agents.entity.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    List<Conversation> findByAgent(Agent agent, Pageable pageable);
}
