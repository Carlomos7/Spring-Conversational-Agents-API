package com.carlomos.agents.controller;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carlomos.agents.dto.request.conversation.ConversationRequest;
import com.carlomos.agents.dto.response.conversation.ConversationResponse;
import com.carlomos.agents.mapper.ConversationMapper;
import com.carlomos.agents.service.ConversationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/conversations")
@Tag(name = "Conversations", description = "Operations related to conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @Operation(summary = "Get conversation by ID", description = "Retrieve a specific conversation by its ID")
    @GetMapping("/{id}")
    public ConversationResponse get(@PathVariable UUID id) {
        return ConversationMapper.toResponse(conversationService.findById(id));
    }

    @Operation(summary = "List conversations by agent", description = "Retrieve a paginated list of conversations for a specific agent")
    @GetMapping
    public List<ConversationResponse> listByAgent(
            @RequestParam UUID agentId,
            @ParameterObject @PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ConversationMapper.toResponseList(conversationService.listByAgent(agentId, pageable));
    }

    @Operation(summary = "Create a new conversation", description = "Create a new conversation for a specific agent")
    @PostMapping
    public ResponseEntity<ConversationResponse> create(@RequestBody ConversationRequest request) {
        var conversation = conversationService.createForAgent(request.agentId());
        return ResponseEntity.ok(ConversationMapper.toResponse(conversation));
    }

    @Operation(summary = "Delete a conversation", description = "Delete an existing conversation by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        conversationService.deleteById(id);
        return ResponseEntity.ok("Conversation deleted successfully");
    }

}
