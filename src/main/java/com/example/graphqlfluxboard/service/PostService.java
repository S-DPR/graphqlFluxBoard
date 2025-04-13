package com.example.graphqlfluxboard.service;

import com.example.graphqlfluxboard.domain.Post;
import com.example.graphqlfluxboard.dto.PostInput;
import com.example.graphqlfluxboard.repos.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<Post> findById(String postId) {
        return postRepository.findById(postId);
    }

    public Flux<Post> findAll() {
        return postRepository.findAll();
    }

    public Mono<Post> save(Post post) {
        return postRepository.save(post);
    }

    public Mono<Post> save(PostInput postInput) {
        String password = passwordEncoder.encode("sa" + postInput.getPassword() + "lt");
        return save(Post.of(postInput, password));
    }

    public Mono<Void> deleteById(String id) {
        return postRepository.deleteById(id);
    }

    public Mono<Void> deleteById(String id, String password) {
        return findById(id)
                .flatMap(post -> {
                    if (checkPassword(post.getPassword(), password)) {
                        return postRepository.delete(post);
                    }
                    return Mono.error(new RuntimeException("Invalid password"));
                });
    }

    public boolean checkPassword(String raw, String hashed) {
        return passwordEncoder.matches("sa" + raw + "lt", hashed);
    }
}
