package com.example.graphqlfluxboard.user.resolver;

import com.example.graphqlfluxboard.user.domain.User;
import com.example.graphqlfluxboard.user.dto.UserInput;
import com.example.graphqlfluxboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserResolver {
    private final UserService userService;

    @QueryMapping
    public Flux<User> users() {
        return userService.findAll();
    }

    @QueryMapping
    public Mono<User> user(@Argument String userId) {
        return userService.findById(userId);
    }

    @MutationMapping
    public Mono<User> createUser(@Argument UserInput userInput) {
        return userService.save(userInput);
    }

    @MutationMapping
    public Mono<Boolean> deleteUser(@Argument String userId) {
        return userService.deleteById(userId).thenReturn(true);
    }
}
