package com.example.graphqlfluxboard.reply.resolver;

import com.example.graphqlfluxboard.reply.domain.Reply;
import com.example.graphqlfluxboard.reply.dto.ReplyInput;
import com.example.graphqlfluxboard.reply.sevice.ReplyService;
import com.example.graphqlfluxboard.user.domain.User;
import com.example.graphqlfluxboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ReplyResolver {
    private final ReplyService replyService;
    private final UserService userService;

    @QueryMapping
    public Flux<Reply> replies() {
        return replyService.replies();
    }

    @QueryMapping
    public Mono<Reply> reply(@Argument String replyId) {
        return replyService.reply(replyId);
    }

    @MutationMapping
    public Mono<Reply> createReply(@Argument ReplyInput replyInput) {
        return replyService.saveReply(replyInput);
    }

    @MutationMapping
    public Mono<Boolean> deleteReply(@Argument String replyId, @Argument String password) {
        return replyService.deleteById(replyId, password).thenReturn(true);
    }

    @BatchMapping(field = "user", typeName = "Reply")
    public Mono<Map<Reply, User>> getUsers(List<Reply> replies) {
        List<String> userIds = replies.stream().map(Reply::getUserId).distinct().toList();
        return userService.findAllByIds(userIds)
                .collectMap(User::getId)
                .map(user -> {
                    return replies.stream().collect(Collectors.toMap(reply -> reply, reply -> user.get(reply.getUserId())));
                });
    }
}
