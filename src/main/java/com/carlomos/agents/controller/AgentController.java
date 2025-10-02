package com.carlomos.agents.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlomos.agents.dto.request.agent.CreateAgentDTO;
import com.carlomos.agents.dto.response.agent.AgentResponseDTO;
import com.carlomos.agents.entity.Agent;
import com.carlomos.agents.mapper.AgentMapper;
import com.carlomos.agents.service.AgentService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("/agents")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping
    public List<AgentResponseDTO> getAllAgents() {
        return agentService.findAll().stream().map(AgentMapper::toDTO).toList();
    }

    @GetMapping("/{id}")
    public AgentResponseDTO getAgentById(@PathVariable UUID id) {
        return AgentMapper.toDTO(agentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AgentResponseDTO> createAgent(@Valid @RequestBody CreateAgentDTO agent) {
        Agent createdAgent = agentService.create(AgentMapper.toEntity(agent));
        return new ResponseEntity<>(AgentMapper.toDTO(createdAgent), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentResponseDTO> updateAgent(@PathVariable UUID id, @Valid @RequestBody CreateAgentDTO agent) {
        Agent updatedAgent = agentService.update(id, AgentMapper.toEntity(agent));
        if (updatedAgent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(AgentMapper.toDTO(updatedAgent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAgent(@PathVariable UUID id) {
        agentService.deleteById(id);
        return ResponseEntity.ok("Agent deleted successfully");
    }
}
