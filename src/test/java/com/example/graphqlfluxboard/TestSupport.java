package com.example.graphqlfluxboard;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class TestSupport {
    protected String TEST_PASSWORD = "TestPW";
    protected String WRONG_PASSWORD = "WRONG";

    @Autowired
    protected CommentService commentService;
    @Autowired
    protected PostService postService;
    @Autowired
    protected ReplyService replyService;
    @Autowired
    protected UserService userService;

    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected ReplyRepository replyRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected PostRepository postRepository;

    @BeforeEach
    public void setUp() {
        commentRepository.deleteAll().block();
        postRepository.deleteAll().block();
        userRepository.deleteAll().block();
        replyRepository.deleteAll().block();
    }

    protected User saveUser(String username, String pw) {
        SaveUserInput userInput = new SaveUserInput();
        userInput.setUsername(username);
        userInput.setPassword(pw);
        User user = userService.createUser(userInput).block();

        notNullTest("User", user);
        equalTest("User", "username", username, user.getUsername());
        return user;
    }

    protected Post savePost(String title, String content, String userId, String pw) {
        SavePostInput postInput = new SavePostInput();
        postInput.setTitle(title);
        postInput.setContent(content);
        postInput.setPassword(pw);
        postInput.setUserId(userId);
        Post post = postService.createPost(postInput).block();

        notNullTest("Post", post);
        equalTest("Post", "title", title, post.getTitle());
        equalTest("Post", "content", content, post.getContent());
        equalTest("Post", "userId", userId, post.getUserId());
        return post;
    }

    protected Comment saveComment(String postId, String comment, String userId, String pw) {
        SaveCommentInput commentInput = new SaveCommentInput();
        commentInput.setPostId(postId);
        commentInput.setUserId(userId);
        commentInput.setPassword(pw);
        commentInput.setComment(comment);
        Comment comment_ = commentService.createComment(commentInput).block();

        notNullTest("Comment", comment_);
        equalTest("Comment", "postId", postId, comment_.getPostId());
        equalTest("Comment", "comment", comment, comment_.getComment());
        equalTest("Comment", "userId", userId, comment_.getUserId());
        return comment_;
    }

    protected Reply saveReply(String commentId, String content, String userId, String pw) {
        SaveReplyInput replyInput = new SaveReplyInput();
        replyInput.setCommentId(commentId);
        replyInput.setContent(content);
        replyInput.setUserId(userId);
        replyInput.setPassword(pw);
        Reply reply = replyService.createReply(replyInput).block();

        notNullTest("Reply", reply);
        equalTest("Reply", "commentId", commentId, reply.getCommentId());
        equalTest("Reply", "content", content, reply.getContent());
        equalTest("Reply", "userId", userId, reply.getUserId());
        return reply;
    }

    private void notNullTest(String domainName, Object domain) {
        Assertions.assertThat(domain)
                .as(String.format("%s not saved", domainName))
                .isNotNull();
    }

    private <T> void equalTest(String domainName, String fieldName, T expected, T actual) {
        Assertions.assertThat(expected)
                .as(String.format("%s %s is not equal", domainName, fieldName))
                .isEqualTo(actual);
    }
}
