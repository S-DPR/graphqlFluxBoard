package com.example.graphqlfluxboard.comment;

import com.example.graphqlfluxboard.comment.domain.Comment;
import com.example.graphqlfluxboard.comment.dto.CommentInput;
import com.example.graphqlfluxboard.comment.service.CommentService;
import com.example.graphqlfluxboard.post.domain.Post;
import com.example.graphqlfluxboard.post.dto.PostInput;
import com.example.graphqlfluxboard.post.service.PostService;
import com.example.graphqlfluxboard.reply.domain.Reply;
import com.example.graphqlfluxboard.reply.dto.ReplyInput;
import com.example.graphqlfluxboard.reply.sevice.ReplyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
public class CommentTest {
    @Autowired private CommentService commentService;
    @Autowired private PostService postService;
    @Autowired private ReplyService replyService;

    public Post savePost(String title, String content, String author, String pw) {
        PostInput postInput = new PostInput();
        postInput.setTitle(title);
        postInput.setContent(content);
        postInput.setAuthorName(author);
        postInput.setPassword(pw);
        Post post = postService.save(postInput).block();
        Assertions.assertThat(post).isNotNull();
        post.setCreatedAt(null);
        return post;
    }

    public Comment saveComment(String postId, String comment, String author, String pw) {
        CommentInput commentInput = new CommentInput();
        commentInput.setPostId(postId);
        commentInput.setAuthorName(author);
        commentInput.setPassword(pw);
        commentInput.setComment(comment);
        Comment comment_ = commentService.saveComment(commentInput).block();
        Assertions.assertThat(comment_).isNotNull();
        comment_.setCreatedAt(null);
        return comment_;
    }

    public Reply saveReply(String commentId, String content, String author, String pw) {
        ReplyInput replyInput = new ReplyInput();
        replyInput.setCommentId(commentId);
        replyInput.setContent(content);
        replyInput.setAuthorName(author);
        replyInput.setPassword(pw);
        Reply reply = replyService.saveReply(replyInput).block();
        Assertions.assertThat(reply).isNotNull();
        reply.setCreatedAt(null);
        return reply;
    }

    @Test
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
    public void getCommentWithRepliesFluxLikeTest() {
        Post post = savePost("Test1", "TestContent", "TestUser", "TestPW");
        Comment comment1 = saveComment(post.getId(), "TestComment", "TestUser", "TestPW");
        Reply reply1 = saveReply(comment1.getId(), "TestUser", "TestReplyComment", "TestPW");
        Reply reply2 = saveReply(comment1.getId(), "TestUser", "TestReplyComment", "TestPW");
        Comment comment2 = saveComment(post.getId(), "TestComment", "TestUser", "TestPW");
        Reply reply3 = saveReply(comment2.getId(), "TestUser", "TestReplyComment", "TestPW");
        Map<String, List<Reply>> expectedRepliesByCommentId = Map.of(comment1.getId(), List.of(reply1, reply2), comment2.getId(), List.of(reply3));

        StepVerifier.create(
                commentService.getCommentByPostId(post.getId()).collectList()
                        .flatMap(this::groupRepliesByCommentId)
        )
        .assertNext(data -> {
            Assertions.assertThat(data)
                    .containsOnlyKeys(expectedRepliesByCommentId.keySet()) // 오 이런거도있네 신기한데
                    .allSatisfy((commentId, replies) -> {
                        Assertions.assertThat(replies)
                                .as("Replies for commentId: %s", commentId) // 실패시 이거 띄워준다
                                .hasSize(expectedRepliesByCommentId.get(commentId).size())
                                .containsExactlyInAnyOrderElementsOf(expectedRepliesByCommentId.get(commentId));
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
