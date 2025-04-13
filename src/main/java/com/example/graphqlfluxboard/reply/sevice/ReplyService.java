package com.example.graphqlfluxboard.reply.sevice;

import com.example.graphqlfluxboard.reply.domain.Reply;
import com.example.graphqlfluxboard.reply.dto.ReplyInput;
import com.example.graphqlfluxboard.reply.repos.ReplyRepository;
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
        String password = passwordService.encryptPassword(replyInput.getPassword());
        return saveReply(Reply.of(replyInput, password));
    }

    public Mono<Void> deleteReply(String replyId) {
        return replyRepository.deleteById(replyId);
    }
}
