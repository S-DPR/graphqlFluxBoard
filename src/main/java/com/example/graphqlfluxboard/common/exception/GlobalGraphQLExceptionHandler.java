package com.example.graphqlfluxboard.common.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

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

        return GraphqlErrorBuilder.newError()
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .message("알 수 없는 에러래요~: " + ex.getMessage())
                .extensions(Map.of(
                        "code", "INTERNAL_ERROR",
                        "exception", ex.getClass().getSimpleName()
                ))
                .build();
    }
}
