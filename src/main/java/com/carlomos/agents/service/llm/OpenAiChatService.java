package com.carlomos.agents.service.llm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    public OpenAiChatService(ChatModel chatModel, AgentRepository agents, ConversationRepository conversations,
            MessageRepository messages) {
        this.chatModel = chatModel;
        this.agents = agents;
        this.conversations = conversations;
        this.messages = messages;
    }

    // Send a user msg and persist the AI reply; enforcing Agent.response_shape if
    // present
    public Message ask(UUID conversationId, String userContent) {
        Conversation conv = conversations.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found: " + conversationId));
        Agent agent = conv.getAgent();

        // Build prompt (system + context + optional hint + recent history)
        List<ChatMessage> prompt = new ArrayList<>();
        if (notBlank(agent.getInstructions()))
            prompt.add(new SystemMessage(agent.getInstructions()));
        if (notBlank(agent.getContext()))
            prompt.add(new SystemMessage("Context: " + agent.getContext()));
        if (notBlank(agent.getFirstMessage()))
            prompt.add(new SystemMessage("First message hint: " + agent.getFirstMessage()));

        var history = messages.findByConversation(
                conv, PageRequest.of(0, 30, Sort.by(Sort.Direction.DESC, "createdAt")));
        // push oldest to newest
        history.stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .forEach(m -> {
                    switch (m.getRole()) {
                        case "user" -> prompt.add(UserMessage.from(m.getContent()));
                        case "agent" -> prompt.add(AiMessage.from(m.getContent()));
                        case "system" -> prompt.add(new SystemMessage(m.getContent()));
                        default -> {
                            // nothing
                        }
                    }
                });
        // persist + add the fresh user msg
        Message userMsg = messages.save(new Message(conv, "user", userContent));
        prompt.add(UserMessage.from(userMsg.getContent()));

        // Build ChatRequest - attach response_format json_schema if present
        ChatRequest chatRequest = ChatRequest.builder()
                .messages(prompt)
                .parameters(buildOpenAiParams(agent))
                .build();
        
        ChatResponse chatResponse = chatModel.chat(chatRequest);

        String aiText = chatResponse.aiMessage().text(); // text() is JSON if schema was enforced

        return messages.save(new Message(conv, "agent", aiText));
    }

    private static boolean notBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private static String safeSchemaName(String name) {
        if (name == null || name.isBlank())
            return "AgentResponse";
        return name.replaceAll("[^A-Za-z0-9_\\-]", "_");
    }

    private Map<String, Object> buildResponseFormatCustomParam(Agent agent) {
        JsonNode schemaNode = agent.getResponseShape(); // JSON Schema stored in DB
        Map<String, Object> jsonSchema = new HashMap<>();
        jsonSchema.put("name", safeSchemaName(agent.getName())); // any safe identifier
        jsonSchema.put("schema", mapper.convertValue(schemaNode, Map.class));
        jsonSchema.put("strict", true); // enforce schema strictly

        return Map.of(
                "response_format", Map.of(
                        "type", "json_schema",
                        "json_schema", jsonSchema));
    }

    private OpenAiChatRequestParameters buildOpenAiParams(Agent agent) {
        if (agent.getResponseShape() == null) {
            return OpenAiChatRequestParameters.builder().build();
        }
        Map<String, Object> custom = buildResponseFormatCustomParam(agent);
        return OpenAiChatRequestParameters.builder()
                .customParameters(custom)
                .build();
    }
}
