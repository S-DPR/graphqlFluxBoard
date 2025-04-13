package com.example.graphqlfluxboard.comment.resolver;

import com.example.graphqlfluxboard.comment.domain.Comment;
import com.example.graphqlfluxboard.comment.dto.CommentInput;
import com.example.graphqlfluxboard.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentResolver {
    private final CommentService commentService;

    @QueryMapping
    public Flux<Comment> comments() {
        return commentService.getComments();
    }

    @QueryMapping
    public Mono<Comment> comment(@Argument String commentId) {
        return commentService.getComment(commentId);
    }

    @MutationMapping
    public Mono<Comment> createComment(@Argument CommentInput commentInput) {
        return commentService.saveComment(commentInput);
    }

    @MutationMapping
    public Mono<Boolean> deleteComment(@Argument String commentId, @Argument String password) {
        return commentService.deleteComment(commentId).thenReturn(true);
    }
}
