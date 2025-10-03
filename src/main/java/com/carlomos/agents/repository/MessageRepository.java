package com.carlomos.agents.repository;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carlomos.agents.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByConversationOrderByCreatedAtAsc(UUID conversationId, Pageable pageable);
}
