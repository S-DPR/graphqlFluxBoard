package com.example.graphqlfluxboard.comment.service;

import com.example.graphqlfluxboard.comment.domain.Comment;
import com.example.graphqlfluxboard.comment.dto.CommentInput;
import com.example.graphqlfluxboard.comment.repos.CommentRepository;
import com.example.graphqlfluxboard.user.service.UserService;
import com.example.graphqlfluxboard.utils.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PasswordService passwordService;

    public Flux<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Mono<Comment> getComment(String id) {
        return commentRepository.findById(id);
    }

    public Flux<Comment> getCommentByPostId(String postId) {
        return commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId);
    }

    public Mono<Comment> saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Mono<Comment> saveComment(CommentInput commentInput) {
        return userService.verify(commentInput.getUserId(), commentInput.getPassword())
                .then(saveComment(Comment.of(commentInput)));
    }

    public Mono<Void> deleteComment(String id) {
        return commentRepository.deleteById(id);
    }

    public Mono<Void> deleteById(String id, String password) {
        return getComment(id)
                .flatMap(comment -> userService.verify(comment.getUserId(), password))
                .then(deleteComment(id));
    }
}
