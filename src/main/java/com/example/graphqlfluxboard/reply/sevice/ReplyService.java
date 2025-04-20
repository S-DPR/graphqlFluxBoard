package com.example.graphqlfluxboard.reply.sevice;

import com.example.graphqlfluxboard.comment.service.CommentService;
import com.example.graphqlfluxboard.common.exception.impl.DeleteFailException;
import com.example.graphqlfluxboard.common.exception.impl.NotFound;
import com.example.graphqlfluxboard.common.exception.enums.Resources;
import com.example.graphqlfluxboard.common.validation.ExistenceValidator;
import com.example.graphqlfluxboard.reply.domain.Reply;
import com.example.graphqlfluxboard.reply.dto.DeleteReplyInput;
import com.example.graphqlfluxboard.reply.dto.SaveReplyInput;
import com.example.graphqlfluxboard.reply.repos.ReplyRepository;
import com.example.graphqlfluxboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final UserService userService;
    private final ExistenceValidator existenceValidator;

    public Flux<Reply> findAllReplies() {
        return replyRepository.findAll();
    }

    public Mono<Reply> findReplyById(String replyId) {
        return replyRepository.findById(replyId)
                .switchIfEmpty(Mono.error(new NotFound(Resources.REPLY)));
    }

    public Flux<Reply> findAllRepliesByCommentIds(List<String> commentIds) {
        return replyRepository.findByCommentIdIn(commentIds);
    }

    public Mono<Reply> createReply(Reply reply) {
        return replyRepository.save(reply);
    }

    public Mono<Reply> createReply(SaveReplyInput saveReplyInput) {
        return userService.verify(saveReplyInput.getUserId(), saveReplyInput.getPassword())
                .then(existenceValidator.validateCommentExists(saveReplyInput.getCommentId()))
                .then(createReply(Reply.of(saveReplyInput)));
    }

    public Mono<Void> deleteReply(String replyId) {
        return replyRepository.deleteById(replyId);
    }

    public Mono<Void> deleteReply(DeleteReplyInput deleteReplyInput) {
        String replyId = deleteReplyInput.getReplyId();
        String password = deleteReplyInput.getPassword();
        return findReplyById(replyId)
                .flatMap(reply -> userService.verify(reply.getUserId(), password))
                .then(deleteReply(replyId));
    }

    public Mono<Void> deleteReplyByCommentIds(List<String> commentIds) {
        if (commentIds.isEmpty()) return Mono.empty();
        return replyRepository.deleteByCommentIdIn(commentIds)
                .onErrorMap(e -> new DeleteFailException(Resources.REPLY));
    }
}
