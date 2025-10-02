package com.carlomos.agents.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carlomos.agents.entity.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, UUID> {

    Optional<Agent> findByName(String name);
    
}
