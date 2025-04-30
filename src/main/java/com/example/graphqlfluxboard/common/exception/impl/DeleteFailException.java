package com.example.graphqlfluxboard.common.exception.impl;

import com.example.graphqlfluxboard.common.exception.ApplicationError;
import com.example.graphqlfluxboard.common.exception.enums.ErrorCode;
import com.example.graphqlfluxboard.common.exception.enums.Resources;

public class DeleteFailException extends ApplicationError {
  private final Resources resources;
  public DeleteFailException(Resources resources) {
    this.resources = resources;
  }

  @Override
  public String getErrorCode() {
    return ErrorCode.DeleteError.getErrorCode();
  }

  @Override
  public String getDefaultMessage() {
    return resources.getValue() + ": Fail to Delete";
  }
}
