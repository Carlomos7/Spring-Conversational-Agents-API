package com.carlomos.agents.mapper;

import com.carlomos.agents.dto.request.agent.AgentRequest;
import com.carlomos.agents.dto.response.agent.AgentResponse;
import com.carlomos.agents.entity.Agent;

public class AgentMapper {

    public static Agent toEntity(AgentRequest dto) {
        return new Agent(
            dto.name(),
            dto.context(),
            dto.firstMessage(),
            dto.responseShape(),
            dto.instructions()
        );
    }

    public static AgentResponse toDTO(Agent agent) {
        return new AgentResponse(
            agent.getId(),
            agent.getName(),
            agent.getContext(),
            agent.getFirstMessage(),
            agent.getResponseShape(),
            agent.getInstructions()
        );
    }
}
