package com.example.graphqlfluxboard.common.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super("Authentication: " + message);
    }
}
