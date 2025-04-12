package com.example.graphqlfluxboard.repos;

import com.example.graphqlfluxboard.domain.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {
}
