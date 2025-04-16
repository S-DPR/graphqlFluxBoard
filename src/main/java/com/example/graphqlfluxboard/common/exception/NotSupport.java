package com.example.graphqlfluxboard.common.exception;

public class NotSupport extends RuntimeException {
    public NotSupport(String message) {
        super("Not Support: " + message);
    }
}
