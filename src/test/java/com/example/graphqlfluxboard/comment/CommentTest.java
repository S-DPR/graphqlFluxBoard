package com.example.graphqlfluxboard.comment;

import com.example.graphqlfluxboard.comment.domain.Comment;
import com.example.graphqlfluxboard.comment.dto.SaveCommentInput;
import com.example.graphqlfluxboard.comment.repos.CommentRepository;
import com.example.graphqlfluxboard.comment.service.CommentService;
import com.example.graphqlfluxboard.post.domain.Post;
import com.example.graphqlfluxboard.post.dto.SavePostInput;
import com.example.graphqlfluxboard.post.repos.PostRepository;
import com.example.graphqlfluxboard.post.service.PostService;
import com.example.graphqlfluxboard.reply.domain.Reply;
import com.example.graphqlfluxboard.reply.dto.SaveReplyInput;
import com.example.graphqlfluxboard.reply.repos.ReplyRepository;
import com.example.graphqlfluxboard.reply.sevice.ReplyService;
import com.example.graphqlfluxboard.user.domain.User;
import com.example.graphqlfluxboard.user.dto.SaveUserInput;
import com.example.graphqlfluxboard.user.repos.UserRepository;
import com.example.graphqlfluxboard.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired CommentService commentService;
    @Autowired PostService postService;
    @Autowired ReplyService replyService;
    @Autowired UserService userService;

    @Autowired CommentRepository commentRepository;
    @Autowired ReplyRepository replyRepository;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll().block();
        postRepository.deleteAll().block();
        userRepository.deleteAll().block();
        replyRepository.deleteAll().block();
    }

    public User saveUser(String username, String pw) {
        SaveUserInput userInput = new SaveUserInput();
        userInput.setUsername(username);
        userInput.setPassword(pw);
        User user = userService.save(userInput).block();
        Assertions.assertThat(user).isNotNull();
        return user;
    }

    public Post savePost(String title, String content, String userId, String pw) {
        SavePostInput postInput = new SavePostInput();
        postInput.setTitle(title);
        postInput.setContent(content);
        postInput.setPassword(pw);
        postInput.setUserId(userId);
        Post post = postService.save(postInput).block();
        Assertions.assertThat(post).isNotNull();
        return post;
    }

    public Comment saveComment(String postId, String comment, String userId, String pw) {
        SaveCommentInput commentInput = new SaveCommentInput();
        commentInput.setPostId(postId);
        commentInput.setUserId(userId);
        commentInput.setPassword(pw);
        commentInput.setComment(comment);
        Comment comment_ = commentService.saveComment(commentInput).block();
        Assertions.assertThat(comment_).isNotNull();
        return comment_;
    }

    public Reply saveReply(String commentId, String content, String userId, String pw) {
        SaveReplyInput replyInput = new SaveReplyInput();
        replyInput.setCommentId(commentId);
        replyInput.setContent(content);
        replyInput.setUserId(userId);
        replyInput.setPassword(pw);
        Reply reply = replyService.saveReply(replyInput).block();
        Assertions.assertThat(reply).isNotNull();
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
        User user = saveUser("TestUser", "TestPW");
        String userId = user.getId();

        Post post = savePost("Test1", "TestContent", userId, "TestPW");
        Comment comment1 = saveComment(post.getId(), "TestComment", userId, "TestPW");
        Reply reply1 = saveReply(comment1.getId(), "TestReply1", userId, "TestPW");
        Reply reply2 = saveReply(comment1.getId(), "TestReply2", userId, "TestPW");
        Comment comment2 = saveComment(post.getId(), "TestComment", userId, "TestPW");
        Reply reply3 = saveReply(comment2.getId(), "TestReply3", userId, "TestPW");
        Map<String, List<Reply>> expectedRepliesByCommentId = Map.of(
                comment1.getId(), List.of(reply1, reply2),
                comment2.getId(), List.of(reply3)
        );

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
                                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt")
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
