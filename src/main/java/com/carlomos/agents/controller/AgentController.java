package com.carlomos.agents.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlomos.agents.dto.request.agent.AgentRequest;
import com.carlomos.agents.dto.response.agent.AgentResponse;
import com.carlomos.agents.entity.Agent;
import com.carlomos.agents.mapper.AgentMapper;
import com.carlomos.agents.service.AgentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Agents", description = "CRUD operations for managing agents")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @Operation(summary = "Get all agents", description = "Retrieve a list of all agents")
    @GetMapping
    public List<AgentResponse> getAllAgents() {
        return agentService.findAll().stream().map(AgentMapper::toResponse).toList();
    }

    @Operation(summary = "Get agent by ID", description = "Retrieve a specific agent by its ID")
    @GetMapping("/{id}")
    public AgentResponse getAgentById(@PathVariable UUID id) {
        return AgentMapper.toResponse(agentService.findById(id));
    }

    @Operation(summary = "Create a new agent", description = "Create a new agent with the provided details")
    @PostMapping
    public ResponseEntity<AgentResponse> createAgent(@Valid @RequestBody AgentRequest agent) {
        Agent createdAgent = agentService.create(AgentMapper.toEntity(agent));
        return new ResponseEntity<>(AgentMapper.toResponse(createdAgent), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing agent", description = "Update the details of an existing agent by its ID")
    @PutMapping("/{id}")
    public ResponseEntity<AgentResponse> updateAgent(@PathVariable UUID id, @Valid @RequestBody AgentRequest agent) {
        Agent existing = agentService.findById(id);
        AgentMapper.copy(agent, existing);
        Agent updatedAgent = agentService.update(id, existing);
        return ResponseEntity.ok(AgentMapper.toResponse(updatedAgent));
    }

    @Operation(summary = "Delete an agent", description = "Delete an existing agent by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAgent(@PathVariable UUID id) {
        agentService.deleteById(id);
        return ResponseEntity.ok("Agent deleted successfully");
    }
}
