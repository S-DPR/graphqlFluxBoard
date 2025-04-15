package com.example.graphqlfluxboard.post.resolver;

import com.example.graphqlfluxboard.post.domain.Post;
import com.example.graphqlfluxboard.post.dto.PostFilterInput;
import com.example.graphqlfluxboard.post.dto.PostInput;
import com.example.graphqlfluxboard.post.service.PostService;
import com.example.graphqlfluxboard.user.domain.User;
import com.example.graphqlfluxboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostResolver {
    private final PostService postService;
    private final UserService userService;

    @QueryMapping
    public Flux<Post> posts(@Argument PostFilterInput postFilterInput) {
        return postService.findAll(postFilterInput);
    }

    @QueryMapping
    public Mono<Post> post(@Argument String postId) {
        return postService.findById(postId);
    }

    @MutationMapping
    public Mono<Post> createPost(@Argument PostInput postInput) {
        return postService.save(postInput);
    }

    @MutationMapping
    public Mono<Boolean> deletePost(@Argument String postId, @Argument String password) {
        return postService.deleteById(postId, password).thenReturn(true);
    }

    @SchemaMapping(field = "user", typeName = "Post")
    public Mono<User> getUser(Post post) {
        return userService.findById(post.getUserId());
    }
}
