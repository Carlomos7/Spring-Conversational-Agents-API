package com.carlomos.agents.config;

import java.net.http.HttpClient;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import dev.langchain4j.http.client.jdk.JdkHttpClient;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel.OpenAiChatModelBuilder;

@Configuration
@EnableConfigurationProperties(OpenAiProperties.class)
public class OpenAiConfig {

    @Bean
    @Primary
    ChatModel chatModel(OpenAiProperties props) {

        var p = props.getChatModel();

        OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
            .apiKey(p.getApiKey())
            .modelName(p.getModelName())
            .temperature(p.getTemperature())
            .strictTools(p.getStrictTools())
            .logRequests(p.getLogRequests())
            .logResponses(p.getLogResponses());

        if (p.getBaseUrl() != null && !p.getBaseUrl().isBlank()) {
            builder.baseUrl(p.getBaseUrl());
        }

        // Provider-specific HTTP client choices
        if ("lmstudio".equalsIgnoreCase(props.getProvider())) {
            // LM Studio plays nicer with HTTP/1.1
                HttpClient.Builder http = HttpClient.newBuilder()
                        .version(HttpClient.Version.HTTP_1_1);

                var jdk = JdkHttpClient.builder()
                        .httpClientBuilder(http);

            builder.httpClientBuilder(jdk);
        }

        return builder.build();
    }
}
