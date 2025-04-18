package com.example.graphqlfluxboard.common.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GlobalGraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {
    @Override
    public GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        log.error("GraphQL Exception: {}", ex.getMessage(), ex);

        if (ex instanceof ApplicationError appError) {
            return GraphqlErrorBuilder.newError()
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .message(appError.getDefaultMessage())
                    .extensions(Map.of(
                            "code", appError.getErrorCode(),
                            "exception", appError.getClass().getSimpleName()
                    ))
                    .build();
        }

        GraphQLError.Builder<?> builder = GraphQLError.newError()
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation());

        if (ex instanceof ConstraintViolationException e) {
            String errors = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            return builder
                    .message("입력값이 잘못됐대요~: " + errors)
                    .extensions(Map.of(
                            "code", "BAD_REQUEST",
                            "exception", ex.getClass().getSimpleName()
                    ))
                    .build();
        }

        return builder
                .message("알 수 없는 에러래요~: " + ex.getMessage())
                .extensions(Map.of(
                        "code", "INTERNAL_ERROR",
                        "exception", ex.getClass().getSimpleName()
                ))
                .build();
    }
}
