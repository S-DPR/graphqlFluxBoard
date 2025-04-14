package com.example.graphqlfluxboard.board.post.repos;

import com.example.graphqlfluxboard.board.post.domain.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {
}
