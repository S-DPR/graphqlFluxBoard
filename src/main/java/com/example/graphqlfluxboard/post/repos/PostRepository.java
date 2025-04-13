package com.example.graphqlfluxboard.post.repos;

import com.example.graphqlfluxboard.post.domain.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {
}
