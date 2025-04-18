package com.example.graphqlfluxboard.comment;

import com.example.graphqlfluxboard.TestSupport;
import com.example.graphqlfluxboard.comment.domain.Comment;
import com.example.graphqlfluxboard.comment.dto.SaveCommentInput;
import com.example.graphqlfluxboard.common.exception.impl.AuthException;
import com.example.graphqlfluxboard.post.domain.Post;
import com.example.graphqlfluxboard.reply.domain.Reply;
import com.example.graphqlfluxboard.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
public class CommentTest extends TestSupport {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    public void getCommentsWithReplies() {
        Post post = savePost("Test1", "TestContent", "TestUser", "TestPW");
        Comment comment = saveComment(post.getId(), "TestComment", "TestUser", "TestPW");
        Reply reply = saveReply(comment.getId(), "TestUser", "TestReplyComment", "TestPW");

        List<Comment> comments = commentService.getCommentByPostId(post.getId()).collectList().block();
        Assertions.assertThat(comments).isNotNull();

        List<String> commentIds = comments.stream().map(Comment::getId).toList();
        Assertions.assertThat(commentIds).isNotNull();

        Map<String, List<Reply>> data = replyService.findAllByCommentIds(commentIds)
                .groupBy(Reply::getCommentId)
                .flatMap(groupedFlux -> groupedFlux
                        .collectSortedList(Comparator.comparing(Reply::getCreatedAt))
                        .map(list -> Map.entry(groupedFlux.key(), list)))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                .block();
        Assertions.assertThat(data).isNotNull();
        Assertions.assertThat(data.size()).isEqualTo(1);

        data.forEach((commentId, replyList) -> {
            Assertions.assertThat(commentId).isEqualTo(comment.getId());
            Set<String> replyIds = replyList.stream().map(Reply::getId).collect(Collectors.toSet());
            Assertions.assertThat(replyIds).contains(reply.getId());
            System.out.println(commentId + ": " + replyList);
        });
    }

    @Test
    public void saveCommentWithWrongPW() {
        User user = saveUser("TestUser", "TestPW");
        Post post = savePost("Test1", "TestContent", user.getId(), "TestPW");

        SaveCommentInput input = new SaveCommentInput(post.getId(), user.getId(), "WRONG", "TestComment");
        StepVerifier.create(commentService.saveComment(input))
                .expectError(AuthException.class)
                .verify();
    }

    @Test
    public void getCommentWithRepliesFluxLikeTest() {
        User user = saveUser("TestUser", "TestPW");
        String userId = user.getId();

        Post post = savePost("Test1", "TestContent", userId, "TestPW");
        Comment comment1 = saveComment(post.getId(), "TestComment1", userId, "TestPW");
        Reply reply1 = saveReply(comment1.getId(), "TestReply1", userId, "TestPW");
        Reply reply2 = saveReply(comment1.getId(), "TestReply2", userId, "TestPW");
        Comment comment2 = saveComment(post.getId(), "TestComment2", userId, "TestPW");
        Reply reply3 = saveReply(comment2.getId(), "TestReply3", userId, "TestPW");
        Map<String, List<Reply>> expectedRepliesByCommentId = Map.of(
                comment1.getId(), List.of(reply1, reply2),
                comment2.getId(), List.of(reply3)
        );

        StepVerifier.create(
                commentService.getCommentByPostId(post.getId()).collectList()
                        .flatMap(this::groupRepliesByCommentId)
        )
        .assertNext(actualRepliesMap -> {
            expectedRepliesByCommentId.forEach((commentId, expectedReplies) -> {
                List<Reply> actualReplies = actualRepliesMap.get(commentId);

                Assertions.assertThat(actualReplies)
                        .as("Replies for commentId: %s", commentId)
                        .isNotNull()
                        .hasSize(expectedReplies.size())
                        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt")
                        .containsExactlyInAnyOrderElementsOf(expectedReplies);
            });
        })
        .verifyComplete();
    }

    private Mono<Map<String, List<Reply>>> groupRepliesByCommentId(List<Comment> comments) {
        List<String> commentIds = comments.stream().map(Comment::getId).toList();
        return replyService.findAllByCommentIds(commentIds)
                .groupBy(Reply::getCommentId)
                .flatMap(groupedFlux -> groupedFlux
                        .collectSortedList(Comparator.comparing(Reply::getCreatedAt))
                        .map(list -> Map.entry(groupedFlux.key(), list)))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }
}
