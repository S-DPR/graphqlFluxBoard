package com.example.graphqlfluxboard.resolver;

import com.example.graphqlfluxboard.domain.Post;
import com.example.graphqlfluxboard.dto.PostInput;
import com.example.graphqlfluxboard.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class PostResolver {
    private final PostService postService;

    public PostResolver(PostService postService) {
        this.postService = postService;
    }

    @QueryMapping
    public Flux<Post> posts() {
        return postService.findAll();
    }

    @QueryMapping
    public Mono<Post> post(@Argument String id) {
        return postService.findById(id);
    }

    @MutationMapping
    public Mono<Post> createPost(@Argument PostInput postInput) {
        return postService.save(postInput);
    }

    @MutationMapping
    public Mono<Boolean> deletePost(@Argument String id) {
        return postService.deleteById(id).thenReturn(true);
    }
}