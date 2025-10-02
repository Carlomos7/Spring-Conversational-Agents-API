package com.carlomos.agents.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carlomos.agents.entity.Agent;
import com.carlomos.agents.repository.AgentRepository;

@Service
@Transactional
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    public Agent findById(UUID id) {
        return agentRepository.findById(id).orElse(null);
    }

    public Agent create(Agent agent) {
        return agentRepository.save(agent);
    }

    public Agent update(UUID id, Agent agentData) {
        Agent existing = findById(id);
        existing.setName(agentData.getName());
        existing.setContext(agentData.getContext());
        existing.setFirstMessage(agentData.getFirstMessage());
        existing.setResponseShape(agentData.getResponseShape());
        existing.setInstructions(agentData.getInstructions());
        return agentRepository.save(existing);
    }

    public void deleteById(UUID id) {
        agentRepository.deleteById(id);
    }

}
