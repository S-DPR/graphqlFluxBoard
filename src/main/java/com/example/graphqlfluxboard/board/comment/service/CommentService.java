package com.example.graphqlfluxboard.board.comment.service;

import com.example.graphqlfluxboard.board.comment.domain.Comment;
import com.example.graphqlfluxboard.board.comment.dto.CommentInput;
import com.example.graphqlfluxboard.board.comment.repos.CommentRepository;
import com.example.graphqlfluxboard.utils.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
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
        String password = passwordService.encryptPassword(commentInput.getPassword());
        return saveComment(Comment.of(commentInput, password));
    }

    public Mono<Void> deleteComment(String id) {
        return commentRepository.deleteById(id);
    }

    public Mono<Void> deleteById(String id, String password) {
        return getComment(id)
                .flatMap(comment -> {
                    if (passwordService.checkPassword(password, comment.getPassword())) {
                        return deleteComment(id);
                    }
                    return Mono.error(new RuntimeException("Invalid password"));
                });
    }
}
