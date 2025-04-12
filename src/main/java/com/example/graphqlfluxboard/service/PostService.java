package com.example.graphqlfluxboard.service;

import com.example.graphqlfluxboard.domain.Post;
import com.example.graphqlfluxboard.repos.PostRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Mono<Post> findById(String id) {
        return postRepository.findById(id);
    }

    public Flux<Post> findAll() {
        return postRepository.findAll();
    }

    public Mono<Post> save(Post post) {
        return postRepository.save(post);
    }

    public Mono<Void> deleteById(String id) {
        return postRepository.deleteById(id);
    }
}
