package com.routerbase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RouterBaseClient {
    public static final String BASE_URL = "https://routerbase.com/v1";
    public static final String DEFAULT_MODEL = "google/gemini-2.5-flash";

    private final String apiKey;
    private final String baseUrl;
    private final Transport transport;
    private final ObjectMapper mapper;

    public RouterBaseClient(String apiKey) {
        this(apiKey, BASE_URL, new HttpTransport());
    }

    public RouterBaseClient(String apiKey, String baseUrl, Transport transport) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("RouterBase API key is required.");
        }

        this.apiKey = apiKey;
        this.baseUrl = trimTrailingSlash(baseUrl == null ? BASE_URL : baseUrl);
        this.transport = transport == null ? new HttpTransport() : transport;
        this.mapper = new ObjectMapper();
    }

    public ChatCompletionResponse chatCompletion(List<ChatMessage> messages)
            throws IOException, InterruptedException {
        return chatCompletion(new ChatCompletionRequest(DEFAULT_MODEL, messages));
    }

    public ChatCompletionResponse chatCompletion(ChatCompletionRequest request)
            throws IOException, InterruptedException {
        if (request == null || request.getMessages() == null || request.getMessages().isEmpty()) {
            throw new IllegalArgumentException("messages must be a non-empty list.");
        }

        if (request.getModel() == null || request.getModel().trim().isEmpty()) {
            request.setModel(DEFAULT_MODEL);
        }

        return send("POST", "/chat/completions", request, ChatCompletionResponse.class);
    }

    public ModelsResponse listModels() throws IOException, InterruptedException {
        return send("GET", "/models", null, ModelsResponse.class);
    }

    private <T> T send(String method, String path, Object payload, Class<T> responseType)
            throws IOException, InterruptedException {
        String body = payload == null ? null : mapper.writeValueAsString(payload);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + apiKey);
        if (body != null) {
            headers.put("Content-Type", "application/json");
        }

        TransportResponse response = transport.request(method, URI.create(baseUrl + path), headers, body);
        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            throw new RouterBaseException(response.getStatusCode(), extractErrorMessage(response.getBody()));
        }

        return mapper.readValue(response.getBody(), responseType);
    }

    private String extractErrorMessage(String body) {
        if (body == null || body.trim().isEmpty()) {
            return "RouterBase request failed.";
        }

        try {
            ApiError error = mapper.readValue(body, ApiError.class);
            if (error.error != null && error.error.message != null) {
                return error.error.message;
            }
            if (error.msg != null) {
                return error.msg;
            }
        } catch (IOException ignored) {
            return body;
        }

        return body;
    }

    private static String trimTrailingSlash(String value) {
        return value.replaceAll("/+$", "");
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class ApiError {
        public ErrorBody error;
        public String msg;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class ErrorBody {
        public String message;
    }
}
