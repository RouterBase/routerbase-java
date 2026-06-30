package com.routerbase;

public final class TransportResponse {
    private final int statusCode;
    private final String body;

    public TransportResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body == null ? "" : body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
