package com.example.graphqlfluxboard.comment.service;

import com.example.graphqlfluxboard.comment.domain.Comment;
import com.example.graphqlfluxboard.comment.dto.DeleteCommentInput;
import com.example.graphqlfluxboard.comment.dto.SaveCommentInput;
import com.example.graphqlfluxboard.comment.repos.CommentRepository;
import com.example.graphqlfluxboard.common.exception.impl.NotFound;
import com.example.graphqlfluxboard.common.exception.enums.Resources;
import com.example.graphqlfluxboard.post.service.PostService;
import com.example.graphqlfluxboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    public Flux<Comment> findAllComments() {
        return commentRepository.findAll();
    }

    public Mono<Comment> findCommentById(String id) {
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFound(Resources.COMMENT)));
    }

    public Flux<Comment> findCommentByPostId(String postId) {
        return commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId);
    }

    public Mono<Boolean> existsById(String id) {
        return commentRepository.existsById(id);
    }

    public Mono<Comment> createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Mono<Comment> createComment(SaveCommentInput saveCommentInput) {
        return userService.verify(saveCommentInput.getUserId(), saveCommentInput.getPassword())
                .then(postService.existsById(saveCommentInput.getPostId()))
                .flatMap(exist -> {
                    if (exist) {
                        return createComment(Comment.of(saveCommentInput));
                    }
                    return Mono.error(new NotFound(Resources.POST));
                });
    }

    public Mono<Void> deleteComment(String id) {
        return commentRepository.deleteById(id);
    }

    public Mono<Void> deleteComment(DeleteCommentInput deleteCommentInput) {
        String id = deleteCommentInput.getCommentId();
        String password = deleteCommentInput.getPassword();

        return findCommentById(id)
                .flatMap(comment -> userService.verify(comment.getUserId(), password))
                .then(deleteComment(id));
    }
}
