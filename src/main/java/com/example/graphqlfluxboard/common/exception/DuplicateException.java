package com.example.graphqlfluxboard.common.exception;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super("Duplicate: " + message);
    }
}
