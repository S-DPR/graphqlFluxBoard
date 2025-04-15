package com.example.graphqlfluxboard.common.exception;

public class AuthException extends RuntimeException {
    public static String message = "Authentication Failed";
    public AuthException(String message) {
        super("Authentication Failed: " + message);
    }
}
