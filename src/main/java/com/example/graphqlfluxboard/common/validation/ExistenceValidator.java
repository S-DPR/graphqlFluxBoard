package com.example.graphqlfluxboard.common.validation;

import com.example.graphqlfluxboard.comment.repos.CommentRepository;
import com.example.graphqlfluxboard.common.exception.enums.Resources;
import com.example.graphqlfluxboard.common.exception.impl.DeleteFailException;
import com.example.graphqlfluxboard.common.exception.impl.NotFound;
import com.example.graphqlfluxboard.post.repos.PostRepository;
import com.example.graphqlfluxboard.reply.repos.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ExistenceValidator {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    private Mono<Void> validateExistence(Mono<Boolean> existenceCheck, Resources resource) {
        return existenceCheck.flatMap(exists ->
                exists ? Mono.empty() : Mono.error(new NotFound(resource))
        );
    }

    public Mono<Void> validatePostExists(String postId) {
        return validateExistence(postRepository.existsById(postId), Resources.POST);
    }

    public Mono<Void> validateCommentExists(String commentId) {
        return validateExistence(commentRepository.existsById(commentId), Resources.COMMENT);
    }

    public Mono<Void> validateReplyExists(String replyId) {
        return validateExistence(replyRepository.existsById(replyId), Resources.REPLY);
    }
}
