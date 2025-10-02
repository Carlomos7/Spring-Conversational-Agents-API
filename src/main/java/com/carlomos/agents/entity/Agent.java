package com.carlomos.agents.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "agents")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private Instant createdAt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "context", nullable = false)
    private String context;

    @Column(name = "first_message", nullable = false)
    private String firstMessage;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "response_shape", nullable = false, columnDefinition = "jsonb")
    private JsonNode responseShape;

    @Column(name = "instructions", nullable = false)
    private String instructions;

    protected Agent() {
    }

    // Default constructor
    public Agent(
            String name,
            String context,
            String firstMessage,
            JsonNode responseShape,
            String instructions) {
        this.name = name;
        this.context = context;
        this.firstMessage = firstMessage;
        this.responseShape = responseShape;
        this.instructions = instructions;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public String getContext() {
        return context;
    }

    public String getFirstMessage() {
        return firstMessage;
    }

    public JsonNode getResponseShape() {
        return responseShape;
    }

    public String getInstructions() {
        return instructions;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setFirstMessage(String firstMessage) {
        this.firstMessage = firstMessage;
    }

    public void setResponseShape(JsonNode responseShape) {
        this.responseShape = responseShape;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", name='" + name + '\'' +
                ", context='" + context + '\'' +
                ", firstMessage='" + firstMessage + '\'' +
                ", responseShape='" + responseShape + '\'' +
                ", instructions='" + instructions + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Agent))
            return false;
        Agent other = (Agent) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}