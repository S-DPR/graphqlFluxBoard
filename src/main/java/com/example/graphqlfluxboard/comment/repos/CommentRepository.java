package com.example.graphqlfluxboard.comment.repos;

import com.example.graphqlfluxboard.comment.domain.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findAllByPostIdOrderByCreatedAtAsc(String postId);

    Mono<Void> deleteByPostId(String postId);
}
