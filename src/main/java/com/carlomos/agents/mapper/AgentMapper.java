package com.carlomos.agents.mapper;

import java.util.List;

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

    // for PUT/PATCH operations
    public static void copy(AgentRequest dto, Agent target){
        target.setName(dto.name());
        target.setContext(dto.context());
        target.setFirstMessage(dto.firstMessage());
        target.setResponseShape(dto.responseShape());
        target.setInstructions(dto.instructions());
    }

    public static AgentResponse toResponse(Agent agent) {
        return new AgentResponse(
            agent.getId(),
            agent.getName(),
            agent.getContext(),
            agent.getFirstMessage(),
            agent.getResponseShape(),
            agent.getInstructions()
        );
    }

    public static List<AgentResponse> toResponseList(List<Agent> agents) {
        return agents.stream().map(AgentMapper::toResponse).toList();
    }
}
