package com.example.graphqlfluxboard.reply.sevice;

import com.example.graphqlfluxboard.reply.domain.Reply;
import com.example.graphqlfluxboard.reply.dto.ReplyInput;
import com.example.graphqlfluxboard.reply.repos.ReplyRepository;
import com.example.graphqlfluxboard.user.service.UserService;
import com.example.graphqlfluxboard.utils.PasswordService;
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
    private final PasswordService passwordService;

    public Flux<Reply> replies() {
        return replyRepository.findAll();
    }

    public Mono<Reply> reply(String replyId) {
        return replyRepository.findById(replyId);
    }

    public Flux<Reply> findAllByCommentIds(List<String> commentIds) {
        return replyRepository.findByCommentIdIn(commentIds);
    }

    public Mono<Reply> saveReply(Reply reply) {
        return replyRepository.save(reply);
    }

    public Mono<Reply> saveReply(ReplyInput replyInput) {
        return userService.verify(replyInput.getUserId(), replyInput.getPassword())
                .then(saveReply(Reply.of(replyInput)));
    }

    public Mono<Void> deleteReply(String replyId) {
        return replyRepository.deleteById(replyId);
    }

    public Mono<Void> deleteById(String replyId, String password) {
        return reply(replyId)
                .flatMap(reply -> userService.verify(reply.getUserId(), password))
                .then(deleteReply(replyId));
    }
}
