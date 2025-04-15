package com.example.graphqlfluxboard.reply.resolver;

import com.example.graphqlfluxboard.reply.domain.Reply;
import com.example.graphqlfluxboard.reply.dto.ReplyInput;
import com.example.graphqlfluxboard.reply.sevice.ReplyService;
import com.example.graphqlfluxboard.user.domain.User;
import com.example.graphqlfluxboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @SchemaMapping(field = "user", typeName = "Reply")
    public Mono<User> getUser(String userId) {
        return userService.findById(userId);
    }
}
