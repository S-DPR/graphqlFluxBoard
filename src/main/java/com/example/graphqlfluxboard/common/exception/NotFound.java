package com.example.graphqlfluxboard.common.exception;

public class NotFound extends RuntimeException {
    public NotFound(String message) {
        super("Not Found: " + message);
    }
}
