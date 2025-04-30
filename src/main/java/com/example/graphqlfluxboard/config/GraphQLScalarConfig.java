package com.example.graphqlfluxboard.config;

import graphql.language.StringValue;
import graphql.scalars.ExtendedScalars;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.time.Instant;

@Configuration
public class GraphQLScalarConfig {

//    @Bean
//    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
//        return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.DateTime);
//    }
    @Bean
    public GraphQLScalarType dateTime() {
        return GraphQLScalarType.newScalar()
                .name("DateTime") // 중요: 이름이 정확히 "DateTime"이어야 GraphQL에서 이걸 씀
                .description("A custom scalar for java.time.Instant")
                .coercing(new Coercing<Instant, String>() {
                    @Override
                    public String serialize(Object dataFetcherResult) {
                        return ((Instant) dataFetcherResult).toString();
                    }

                    @Override
                    public Instant parseValue(Object input) {
                        return Instant.parse(input.toString());
                    }

                    @Override
                    public Instant parseLiteral(Object input) {
                        return Instant.parse(((StringValue) input).getValue());
                    }
                })
                .build();
    }

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(GraphQLScalarType dateTime) {
        return wiringBuilder -> wiringBuilder.scalar(dateTime); // ← 이걸 DateTime 이름으로 강제 override
    }

}
