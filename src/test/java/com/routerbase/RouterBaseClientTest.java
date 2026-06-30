package com.routerbase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class RouterBaseClientTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void chatCompletionSendsExpectedRequest() throws Exception {
        FakeTransport transport = new FakeTransport(new TransportResponse(
                200,
                "{\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"Hello\"}}]}"));
        RouterBaseClient client = new RouterBaseClient("sk-rb-test", RouterBaseClient.BASE_URL, transport);

        ChatCompletionResponse response = client.chatCompletion(List.of(new ChatMessage("user", "Hi")));
        CapturedRequest request = transport.requests.get(0);
        JsonNode body = mapper.readTree(request.body);

        assertEquals("Hello", response.getChoices().get(0).getMessage().getContent());
        assertEquals("POST", request.method);
        assertEquals("https://routerbase.com/v1/chat/completions", request.uri.toString());
        assertEquals("Bearer sk-rb-test", request.headers.get("Authorization"));
        assertEquals(RouterBaseClient.DEFAULT_MODEL, body.get("model").asText());
    }

    @Test
    void listModelsSendsExpectedRequest() throws Exception {
        FakeTransport transport = new FakeTransport(new TransportResponse(
                200,
                "{\"data\":[{\"id\":\"google/gemini-2.5-flash\"}]}"));
        RouterBaseClient client = new RouterBaseClient("sk-rb-test", RouterBaseClient.BASE_URL, transport);

        ModelsResponse response = client.listModels();
        CapturedRequest request = transport.requests.get(0);

        assertEquals("google/gemini-2.5-flash", response.getData().get(0).getId());
        assertEquals("GET", request.method);
        assertEquals("https://routerbase.com/v1/models", request.uri.toString());
    }

    @Test
    void requiresApiKey() {
        assertThrows(IllegalArgumentException.class, () -> new RouterBaseClient(""));
    }

    @Test
    void requiresMessages() {
        RouterBaseClient client = new RouterBaseClient("sk-rb-test");
        assertThrows(IllegalArgumentException.class, () -> client.chatCompletion(List.of()));
    }

    @Test
    void throwsApiErrors() {
        FakeTransport transport = new FakeTransport(new TransportResponse(
                400,
                "{\"error\":{\"message\":\"bad request\"}}"));
        RouterBaseClient client = new RouterBaseClient("sk-rb-test", RouterBaseClient.BASE_URL, transport);

        RouterBaseException error = assertThrows(RouterBaseException.class, client::listModels);
        assertEquals(400, error.getStatusCode());
        assertEquals("RouterBase request failed (400): bad request", error.getMessage());
    }

    private static final class FakeTransport implements Transport {
        private final TransportResponse response;
        private final List<CapturedRequest> requests = new ArrayList<>();

        private FakeTransport(TransportResponse response) {
            this.response = response;
        }

        @Override
        public TransportResponse request(String method, URI uri, Map<String, String> headers, String body)
                throws IOException, InterruptedException {
            requests.add(new CapturedRequest(method, uri, headers, body));
            return response;
        }
    }

    private static final class CapturedRequest {
        private final String method;
        private final URI uri;
        private final Map<String, String> headers;
        private final String body;

        private CapturedRequest(String method, URI uri, Map<String, String> headers, String body) {
            this.method = method;
            this.uri = uri;
            this.headers = headers;
            this.body = body;
        }
    }
}
