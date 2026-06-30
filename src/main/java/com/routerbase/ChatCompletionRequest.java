package com.routerbase;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public final class ChatCompletionRequest {
    private String model;
    private List<ChatMessage> messages;
    private Double temperature;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    public ChatCompletionRequest() {
    }

    public ChatCompletionRequest(List<ChatMessage> messages) {
        this(RouterBaseClient.DEFAULT_MODEL, messages);
    }

    public ChatCompletionRequest(String model, List<ChatMessage> messages) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }
}
