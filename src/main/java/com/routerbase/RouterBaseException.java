package com.routerbase;

public final class RouterBaseException extends RuntimeException {
    private final int statusCode;

    public RouterBaseException(int statusCode, String message) {
        super("RouterBase request failed (" + statusCode + "): " + message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
