package com.example.graphqlfluxboard.post.resolver;

import com.example.graphqlfluxboard.post.domain.Post;
import com.example.graphqlfluxboard.post.dto.DeletePostInput;
import com.example.graphqlfluxboard.post.dto.PostFilterInput;
import com.example.graphqlfluxboard.post.dto.SavePostInput;
import com.example.graphqlfluxboard.post.service.PostService;
import com.example.graphqlfluxboard.user.domain.User;
import com.example.graphqlfluxboard.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostResolver {
    private final PostService postService;
    private final UserService userService;

    @QueryMapping
    public Flux<Post> findAllPosts(@Argument PostFilterInput postFilterInput) {
        if (postFilterInput == null) {
            postFilterInput = new PostFilterInput();
        }
        return postService.findAllPosts(postFilterInput);
    }

    @QueryMapping
    public Mono<Post> findPostById(@Argument String postId) {
        return postService.findPostById(postId);
    }

    @MutationMapping
    public Mono<Post> createPost(@Valid @Argument SavePostInput savePostInput) {
        return postService.createPost(savePostInput);
    }

    @MutationMapping
    public Mono<Boolean> deletePost(@Valid @Argument DeletePostInput deletePostInput) {
        return postService.deletePost(deletePostInput).thenReturn(true);
    }

    @BatchMapping(field = "user", typeName = "Post")
    public Mono<Map<Post, User>> getUsers(List<Post> posts) {
        List<String> userIds = posts.stream().map(Post::getUserId).distinct().toList();
        return userService.findAllByIds(userIds)
                .collectMap(User::getId)
                .map(userMap -> posts.stream().collect(Collectors.toMap(post -> post, post -> userMap.get(post.getUserId()))));
    }
}
