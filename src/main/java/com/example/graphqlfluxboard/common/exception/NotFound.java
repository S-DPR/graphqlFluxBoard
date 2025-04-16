package com.example.graphqlfluxboard.common.exception;

import com.example.graphqlfluxboard.common.exception.enums.ErrorCode;
import com.example.graphqlfluxboard.common.exception.enums.Resources;

public class NotFound extends ApplicationError {
    private final Resources resources;
    public NotFound(Resources resources) {
        this.resources = resources;
    }

    @Override
    public String getErrorCode() {
        return ErrorCode.NotFoundError.getErrorCode();
    }

    @Override
    public String getDefaultMessage() {
        return resources.getValue() + ": 못찾았대요~";
    }
}
