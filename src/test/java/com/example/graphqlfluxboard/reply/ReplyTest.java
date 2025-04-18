package com.example.graphqlfluxboard.reply;

import com.example.graphqlfluxboard.TestSupport;
import com.example.graphqlfluxboard.comment.domain.Comment;
import com.example.graphqlfluxboard.common.exception.impl.AuthException;
import com.example.graphqlfluxboard.common.exception.impl.NotFound;
import com.example.graphqlfluxboard.post.domain.Post;
import com.example.graphqlfluxboard.reply.domain.Reply;
import com.example.graphqlfluxboard.reply.dto.DeleteReplyInput;
import com.example.graphqlfluxboard.reply.dto.SaveReplyInput;
import com.example.graphqlfluxboard.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
public class ReplyTest extends TestSupport {
    @BeforeEach
    public void setup() {
        super.setUp();
    }

    @Test
    void saveReplyWithWrongPW() {
        User user = saveUser("USERNAME", TEST_PASSWORD);
        Post post = savePost("TITLE", "CONTENT", user.getId(), TEST_PASSWORD);
        Comment comment = saveComment(post.getId(), "COMMENT", user.getId(), TEST_PASSWORD);
        SaveReplyInput saveReplyInput = SaveReplyInput.builder()
                .userId(user.getId())
                .content("REPLY")
                .password(WRONG_PASSWORD)
                .commentId(comment.getId())
                .build();
        StepVerifier.create(replyService.createReply(saveReplyInput))
                .expectError(AuthException.class)
                .verify();
    }

    @Test
    void deleteReplyWithWrongPW() {
        User user = saveUser("USERNAME", TEST_PASSWORD);
        Post post = savePost("TITLE", "CONTENT", user.getId(), TEST_PASSWORD);
        Comment comment = saveComment(post.getId(), "COMMENT", user.getId(), TEST_PASSWORD);
        Reply reply = saveReply(comment.getId(), "REPLY", user.getId(), TEST_PASSWORD);

        DeleteReplyInput deleteReplyInput = DeleteReplyInput.builder()
                .replyId(reply.getId())
                .password(WRONG_PASSWORD)
                .build();
        StepVerifier.create(replyService.deleteReply(deleteReplyInput))
                .expectError(AuthException.class)
                .verify();
        StepVerifier.create(replyService.findReplyById(reply.getId()))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void deleteReply() {
        User user = saveUser("USERNAME", TEST_PASSWORD);
        Post post = savePost("TITLE", "CONTENT", user.getId(), TEST_PASSWORD);
        Comment comment = saveComment(post.getId(), "COMMENT", user.getId(), TEST_PASSWORD);
        Reply reply = saveReply(comment.getId(), "REPLY", user.getId(), TEST_PASSWORD);

        DeleteReplyInput deleteReplyInput = DeleteReplyInput.builder()
                .replyId(reply.getId())
                .password(TEST_PASSWORD)
                .build();
        StepVerifier.create(replyService.deleteReply(deleteReplyInput))
                .verifyComplete();
        StepVerifier.create(replyService.findReplyById(reply.getId()))
                .expectError(NotFound.class)
                .verify();
    }
}
