package com.example.graphqlfluxboard.common.exception;

import com.example.graphqlfluxboard.common.exception.enums.ErrorCode;

public class NotSupport extends ApplicationError {
    private final String nameOfDuplicated;
    public NotSupport(String nameOfDuplicated) {
        this.nameOfDuplicated = nameOfDuplicated;
    }

    @Override
    public String getErrorCode() {
        return ErrorCode.NotSupportError.getErrorCode();
    }

    @Override
    public String getDefaultMessage() {
        return nameOfDuplicated + ": 중복이래요~";
    }
}
