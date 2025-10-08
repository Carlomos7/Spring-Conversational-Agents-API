package com.carlomos.agents.service.llm;

import java.util.*;
import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.carlomos.agents.entity.Agent;
import com.carlomos.agents.entity.Conversation;
import com.carlomos.agents.entity.Message;
import com.carlomos.agents.repository.AgentRepository;
import com.carlomos.agents.repository.ConversationRepository;
import com.carlomos.agents.repository.MessageRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatRequestParameters;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OpenAiChatService {

    private final ChatModel chatModel;
    private final AgentRepository agents;
    private final ConversationRepository conversations;
    private final MessageRepository messages;
    private final ObjectMapper mapper = new ObjectMapper();

    public OpenAiChatService(
            ChatModel chatModel,
            AgentRepository agents,
            ConversationRepository conversations,
            MessageRepository messages) {
        this.chatModel = chatModel;
        this.agents = agents;
        this.conversations = conversations;
        this.messages = messages;
    }

    /**
     * Sends a user message, appends to history, calls OpenAI with optional JSON-Schema
     * (from Agent.responseShape), persists the AI reply, and returns the saved reply.
     */
    public Message ask(UUID conversationId, String userContent) {
        Conversation conv = conversations.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found: " + conversationId));
        Agent agent = conv.getAgent();

        // 1) Build prompt: system→context→firstMessageHint→recent history→current user
        List<ChatMessage> prompt = new ArrayList<>(8);

        if (notBlank(agent.getInstructions())) {
            prompt.add(new SystemMessage(agent.getInstructions()));
        }
        if (notBlank(agent.getContext())) {
            prompt.add(new SystemMessage("Context: " + agent.getContext()));
        }
        if (notBlank(agent.getFirstMessage())) {
            prompt.add(new SystemMessage("First message hint: " + agent.getFirstMessage()));
        }

        var recent = messages.findByConversation(
                conv, PageRequest.of(0, 30, Sort.by(Sort.Direction.DESC, "createdAt")));

        // Add recent messages in chronological order
        recent.stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .forEach(m -> {
                    String role = m.getRole();
                    if ("user".equals(role))        prompt.add(UserMessage.from(m.getContent()));
                    else if ("agent".equals(role))  prompt.add(AiMessage.from(m.getContent()));
                    else if ("system".equals(role)) prompt.add(new SystemMessage(m.getContent()));
                });

        // 2) Persist current user message and add to prompt
        Message userMsg = messages.save(new Message(conv, "user", userContent));
        prompt.add(UserMessage.from(userMsg.getContent()));

        // 3) Build request with JSON-Schema response_format if agent has one
        OpenAiChatRequestParameters params = buildOpenAiParams(agent);

        ChatRequest chatRequest = ChatRequest.builder()
                .messages(prompt)
                .parameters(params)
                .build();

        // Call model
        ChatResponse chatResponse = chatModel.chat(chatRequest);

        // Extract text (should always be present)
        String aiText = chatResponse.aiMessage().text();

        // Persist reply
        return messages.save(new Message(conv, "agent", aiText));
    }

    // ---- helpers -------------------------------------------------------------

    private static boolean notBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static String safeSchemaName(String name) {
        return (name == null || name.isBlank())
                ? "AgentResponse"
                : name.replaceAll("[^A-Za-z0-9_\\-]", "_");
    }

    /**
     * Creates OpenAI custom parameters for response_format=json_schema
     * using the schema stored in Agent.responseShape (must be a valid JSON object).
     *
     * If no schema is present, returns empty/default parameters.
     */
    private OpenAiChatRequestParameters buildOpenAiParams(Agent agent) {
        JsonNode schemaNode = agent.getResponseShape();
        if (schemaNode == null || schemaNode.isNull()) {
            return OpenAiChatRequestParameters.builder().build();
        }
        if (!schemaNode.isObject()) {
            // hard fail early to avoid 400s from OpenAI
            throw new IllegalArgumentException("Agent.responseShape must be a JSON object (valid JSON Schema)");
        }

        // Per OpenAI structured outputs: wrap in { response_format: { type: "json_schema", json_schema: {...} } }
        Map<String, Object> jsonSchema = new HashMap<>();
        jsonSchema.put("name", safeSchemaName(agent.getName())); // arbitrary identifier
        jsonSchema.put("strict", true);
        jsonSchema.put("schema", mapper.convertValue(schemaNode, Map.class)); // deep map view of the schema

        Map<String, Object> custom = Map.of(
            "response_format", Map.of(
                "type", "json_schema",
                "json_schema", jsonSchema
            )
        );

        return OpenAiChatRequestParameters.builder()
                .customParameters(custom)
                .build();
    }
}
