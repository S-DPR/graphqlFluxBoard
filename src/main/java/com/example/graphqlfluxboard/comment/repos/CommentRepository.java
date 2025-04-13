package com.example.graphqlfluxboard.comment.repos;

import com.example.graphqlfluxboard.comment.domain.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
}
