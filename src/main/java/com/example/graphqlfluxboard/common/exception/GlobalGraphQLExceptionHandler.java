package com.example.graphqlfluxboard.common.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Component
public class GlobalGraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {
    private final Map<Class<? extends Throwable>, Function<Throwable, String>> errorMessageHandlers = Map.of(
            AuthException.class, ex -> "인증에러래요~: " + ex.getMessage(),
            DuplicateException.class, ex -> "중복에러래요~: " + ex.getMessage()
    );

    @Override
    public GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError()
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .message(errorMessageHandlers.getOrDefault(ex.getClass(), e -> "알수없는에러래요~: " + e.getMessage()).apply(ex))
                .build();
    }
}
