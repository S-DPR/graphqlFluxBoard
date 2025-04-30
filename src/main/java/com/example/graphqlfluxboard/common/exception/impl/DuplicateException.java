package com.example.graphqlfluxboard.common.exception.impl;

import com.example.graphqlfluxboard.common.exception.ApplicationError;
import com.example.graphqlfluxboard.common.exception.enums.ErrorCode;
import com.example.graphqlfluxboard.common.exception.enums.Resources;

public class DuplicateException extends ApplicationError {
    private final Resources resources;
    public DuplicateException(Resources resources) {
        this.resources = resources;
    }

    @Override
    public String getErrorCode() {
        return ErrorCode.DuplicateError.getErrorCode();
    }

    @Override
    public String getDefaultMessage() {
        return resources.getValue() + ": Duplicated";
    }
}
