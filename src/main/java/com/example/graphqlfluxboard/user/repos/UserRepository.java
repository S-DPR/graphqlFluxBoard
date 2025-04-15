package com.example.graphqlfluxboard.user.repos;

import com.example.graphqlfluxboard.user.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    public Flux<User> findAllByIdIn(List<String> ids);

    Mono<Boolean> existsByUsername(String username);
}
