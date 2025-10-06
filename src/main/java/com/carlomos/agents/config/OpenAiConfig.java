package com.carlomos.agents.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

@Configuration
public class OpenAiConfig {

    @Bean
    ChatModel openAIChatModel(
            @Value("${langchain4j.openai.chat-model.api-key}") String apiKey,
            @Value("${langchain4j.openai.chat-model.model:gpt-4o-mini}") String modelName) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Missing OpenAI API key. Set openai.api-key or OPENAI_API_KEY");
        }
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(0.2)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
