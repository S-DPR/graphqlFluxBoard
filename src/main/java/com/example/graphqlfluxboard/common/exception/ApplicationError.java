package com.example.graphqlfluxboard.common.exception;

public abstract class ApplicationError extends RuntimeException {
    public abstract String getErrorCode();
    public abstract String getDefaultMessage();
}
