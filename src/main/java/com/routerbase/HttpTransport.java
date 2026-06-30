package com.routerbase;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public final class HttpTransport implements Transport {
    private final HttpClient httpClient;

    public HttpTransport() {
        this(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).build());
    }

    public HttpTransport(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public TransportResponse request(String method, URI uri, Map<String, String> headers, String body)
            throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(60));
        headers.forEach(builder::header);

        if ("GET".equalsIgnoreCase(method)) {
            builder.GET();
        } else {
            builder.method(method.toUpperCase(), HttpRequest.BodyPublishers.ofString(body == null ? "" : body));
        }

        HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        return new TransportResponse(response.statusCode(), response.body());
    }
}
