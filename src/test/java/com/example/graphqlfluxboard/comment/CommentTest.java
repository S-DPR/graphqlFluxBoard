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

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CommentTest {
    @Autowired private CommentService commentService;
    @Autowired private PostService postService;
    @Autowired private ReplyService replyService;

    @Test
    public void getCommentsWithReplies() {
        PostInput postInput = new PostInput();
        postInput.setTitle("Test1");
        postInput.setContent("TestContent");
        postInput.setAuthorName("TestUser");
        postInput.setPassword("TestPassword");
        Post post = postService.save(postInput).block();
        Assertions.assertThat(post).isNotNull();

        CommentInput commentInput = new CommentInput();
        commentInput.setComment("TestComment");
        commentInput.setAuthorName("TestUser");
        commentInput.setPassword("TestPassword");
        commentInput.setPostId(post.getId());
        Comment comment = commentService.saveComment(commentInput).block();
        Assertions.assertThat(comment).isNotNull();

        ReplyInput replyInput = new ReplyInput();
        replyInput.setCommentId(comment.getId());
        replyInput.setAuthorName("TestUser");
        replyInput.setPassword("TestPassword");
        replyInput.setContent("TestReplyComment");
        Reply reply = replyService.saveReply(replyInput).block();
        Assertions.assertThat(reply).isNotNull();

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
        data.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
