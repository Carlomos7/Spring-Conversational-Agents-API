package com.carlomos.agents.controller;

import com.carlomos.agents.dto.response.message.MessageResponse;
import com.carlomos.agents.mapper.MessageMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carlomos.agents.service.llm.OpenAiChatService;

import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final OpenAiChatService chatService;

    public ChatController(OpenAiChatService chatService) {
        this.chatService = chatService;
    }

    public record ChatRequest(String content) {
    }

    @Operation(summary = "Send a user message and get structured/text AI reply (per Agent schema)")
    @PostMapping("/conversations/{conversationId}")
    public ResponseEntity<MessageResponse> chat(@PathVariable UUID conversationId,
            @RequestBody ChatRequest body) {
        var aiMsg = chatService.ask(conversationId, body.content());
        return ResponseEntity.ok(MessageMapper.toResponse(aiMsg));
    }
}
