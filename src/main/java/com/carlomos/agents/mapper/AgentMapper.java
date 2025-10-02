package com.carlomos.agents.mapper;

import com.carlomos.agents.dto.request.agent.CreateAgentDTO;
import com.carlomos.agents.dto.response.agent.AgentResponseDTO;
import com.carlomos.agents.entity.Agent;

public class AgentMapper {

    public static Agent toEntity(CreateAgentDTO dto) {
        return new Agent(
            dto.name(),
            dto.context(),
            dto.firstMessage(),
            dto.responseShape(),
            dto.instructions()
        );
    }

    public static AgentResponseDTO toDTO(Agent agent) {
        return new AgentResponseDTO(
            agent.getId(),
            agent.getName(),
            agent.getContext(),
            agent.getFirstMessage(),
            agent.getResponseShape(),
            agent.getInstructions()
        );
    }
}
