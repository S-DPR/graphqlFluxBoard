package com.example.graphqlfluxboard.user.resolver;

import com.example.graphqlfluxboard.user.domain.User;
import com.example.graphqlfluxboard.user.dto.DeleteUserInput;
import com.example.graphqlfluxboard.user.dto.SaveUserInput;
import com.example.graphqlfluxboard.user.service.UserService;
import jakarta.validation.Valid;
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
    public Flux<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @QueryMapping
    public Mono<User> findUserById(@Argument String userId) {
        return userService.findUserById(userId);
    }

    @MutationMapping
    public Mono<User> createUser(@Valid @Argument SaveUserInput saveUserInput) {
        return userService.createUser(saveUserInput);
    }

    @MutationMapping
    public Mono<Boolean> deleteUser(@Valid @Argument DeleteUserInput deleteUserInput) {
        return userService.deleteUser(deleteUserInput).thenReturn(true);
    }
}
