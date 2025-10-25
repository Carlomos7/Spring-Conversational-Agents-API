package com.carlomos.agents.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "langchain4j.open-ai")
public class OpenAiProperties {

    private String provider = "openai";

    private final ChatModel chatModel = new ChatModel();

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public ChatModel getChatModel() { return chatModel; }

    @Validated
    public static class ChatModel {
        @NotBlank
        private String apiKey;

        @NotBlank
        private String modelName;

        // Optional / standard keys
        private String baseUrl = "";
        private Double temperature = 0.2;
        private Boolean logRequests = false;
        private Boolean logResponses = false;
        private Boolean strictTools = true;

        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }

        public String getModelName() { return modelName; }
        public void setModelName(String modelName) { this.modelName = modelName; }

        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

        public Double getTemperature() { return temperature; }
        public void setTemperature(Double temperature) { this.temperature = temperature; }

        public Boolean getLogRequests() { return logRequests; }
        public void setLogRequests(Boolean logRequests) { this.logRequests = logRequests; }

        public Boolean getLogResponses() { return logResponses; }
        public void setLogResponses(Boolean logResponses) { this.logResponses = logResponses; }

        public Boolean getStrictTools() { return strictTools; }
        public void setStrictTools(Boolean strictTools) { this.strictTools = strictTools; }
    }
}
