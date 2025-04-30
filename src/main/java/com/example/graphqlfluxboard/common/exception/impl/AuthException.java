package com.example.graphqlfluxboard.common.exception.impl;

import com.example.graphqlfluxboard.common.exception.ApplicationError;
import com.example.graphqlfluxboard.common.exception.enums.ErrorCode;

public class AuthException extends ApplicationError {
    private final String message;
    public AuthException(String message) {
        this.message = message;
    }

    @Override
    public String getErrorCode() {
        return ErrorCode.AuthenticationError.getErrorCode();
    }

    @Override
    public String getDefaultMessage() {
        return message + ": Auth Error";
    }
}
