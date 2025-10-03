package com.carlomos.agents.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.carlomos.agents.dto.request.message.MessageRequest;
import com.carlomos.agents.dto.response.message.MessageResponse;
import com.carlomos.agents.mapper.MessageMapper;
import com.carlomos.agents.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/messages")
@Tag(name = "Messages", description = "Operations related to messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "Get message by ID", description = "Retrieve a specific message by its ID")
    @GetMapping("/{id}")
    public MessageResponse get(@PathVariable UUID id) {
        return MessageMapper.toResponse(messageService.findById(id));
    }

    @Operation(summary = "List messages by conversation", description = "Retrieve a paginated list of messages for a specific conversation")
    @GetMapping("/conversations/{conversationId}")
    public List<MessageResponse> listByConversation(@PathVariable UUID conversationId, Pageable pageable) {
        return MessageMapper.toResponseList(messageService.listByConversation(conversationId, pageable));
    }

    @PostMapping("/conversations/{conversationId}")
    @Operation(summary = "Append a message to a conversation", description = "Append a new message to a specific conversation")
    public ResponseEntity<MessageResponse> append(
            @PathVariable UUID conversationId,
            @Valid @RequestBody MessageRequest messageRequest) {
        var message = messageService.append(conversationId, messageRequest.role(), messageRequest.content());
        return new ResponseEntity<>(MessageMapper.toResponse(message), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a message", description = "Delete an existing message by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        messageService.deleteById(id);
        return ResponseEntity.ok("Message deleted successfully");
    }

}
