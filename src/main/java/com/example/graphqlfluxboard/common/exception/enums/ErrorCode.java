package com.example.graphqlfluxboard.common.exception.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    AuthenticationError("AUTHENTICATION_ERROR"),
    DuplicateError("DUPLICATE_ERROR"),
    NotFoundError("NOT_FOUND"),
    NotSupportError("NOT_SUPPORT"),
    DeleteError("DELETE_ERROR"),;

    final String errorCode;
    ErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }
}
