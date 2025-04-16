package com.example.graphqlfluxboard.comment.service;

import com.example.graphqlfluxboard.comment.domain.Comment;
import com.example.graphqlfluxboard.comment.dto.CommentInput;
import com.example.graphqlfluxboard.comment.repos.CommentRepository;
import com.example.graphqlfluxboard.common.exception.NotFound;
import com.example.graphqlfluxboard.post.service.PostService;
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
    private final PostService postService;

    public Flux<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Mono<Comment> getComment(String id) {
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFound(id + ": 이 Comment 못찾았대요~")));
    }

    public Flux<Comment> getCommentByPostId(String postId) {
        return commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId);
    }

    public Mono<Boolean> existsById(String id) {
        return commentRepository.existsById(id);
    }

    public Mono<Comment> saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Mono<Comment> saveComment(CommentInput commentInput) {
        return userService.verify(commentInput.getUserId(), commentInput.getPassword())
                .then(Mono.defer(() -> postService.existsById(commentInput.getPostId())))
                .flatMap(exist -> {
                    if (exist) {
                        return saveComment(Comment.of(commentInput));
                    }
                    return Mono.error(new NotFound("게시글이 사라졌대요~"));
                });
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
