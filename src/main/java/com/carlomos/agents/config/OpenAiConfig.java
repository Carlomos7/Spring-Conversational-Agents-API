package com.carlomos.agents.config;

import java.net.http.HttpClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.http.client.jdk.JdkHttpClient;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

@Configuration
public class OpenAiConfig {
    @Bean
    ChatModel openAIChatModel(
            @Value("${langchain4j.open-ai.chat-model.api-key}") String apiKey,
            @Value("${langchain4j.open-ai.chat-model.model-name}") String modelName,
            @Value("${langchain4j.open-ai.chat-model.base-url}") String baseUrl,
            @Value("${openai.strict-tools:true}") boolean strictTools) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Missing OpenAI API key. Set openai.api-key or OPENAI_API_KEY");
        }

        HttpClient.Builder http = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1);

        var jdk = JdkHttpClient.builder()
                .httpClientBuilder(http);

        return OpenAiChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .strictTools(strictTools)
                .temperature(0.2)
                .logRequests(true)
                .logResponses(true)
                .httpClientBuilder(jdk)
                .build();
    }
}