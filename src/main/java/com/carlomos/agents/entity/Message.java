package com.carlomos.agents.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages", indexes = {
        @Index(name = "idx_messages_conv_created", columnList = "conversation_id, created_at DESC")
}

)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false) // FK Constraint
    private Conversation conversation;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private Instant createdAt;

    @Column(name = "role", nullable = false)
    private String role; // expected: "user" | "agent" | "system"

    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    protected Message() {
    }

    // Default constructor
    public Message(Conversation conversation, String role, String content) {
        this.conversation = conversation;
        this.role = role;
        this.content = content;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    // Setters
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", conversationId=" + (conversation != null ? conversation.getId() : null) +
                ", createdAt=" + createdAt +
                ", role='" + role + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Message))
            return false;
        Message other = (Message) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
