package com.example.graphqlfluxboard.comment.service;

import com.example.graphqlfluxboard.comment.domain.Comment;
import com.example.graphqlfluxboard.comment.dto.CommentInput;
import com.example.graphqlfluxboard.comment.repos.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public Flux<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Mono<Comment> getComment(String id) {
        return commentRepository.findById(id);
    }

    public Mono<Comment> saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Mono<Comment> saveComment(CommentInput commentInput) {
        return saveComment(Comment.of(commentInput));
    }

    public Mono<Void> deleteComment(String id) {
        return commentRepository.deleteById(id);
    }
}
