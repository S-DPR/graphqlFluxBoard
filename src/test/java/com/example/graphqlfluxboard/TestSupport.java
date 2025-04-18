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
        User user = userService.save(userInput).block();
        Assertions.assertThat(user).isNotNull();
        return user;
    }

    protected Post savePost(String title, String content, String userId, String pw) {
        SavePostInput postInput = new SavePostInput();
        postInput.setTitle(title);
        postInput.setContent(content);
        postInput.setPassword(pw);
        postInput.setUserId(userId);
        Post post = postService.save(postInput).block();
        Assertions.assertThat(post).isNotNull();
        return post;
    }

    protected Comment saveComment(String postId, String comment, String userId, String pw) {
        SaveCommentInput commentInput = new SaveCommentInput();
        commentInput.setPostId(postId);
        commentInput.setUserId(userId);
        commentInput.setPassword(pw);
        commentInput.setComment(comment);
        Comment comment_ = commentService.saveComment(commentInput).block();
        Assertions.assertThat(comment_).isNotNull();
        return comment_;
    }

    protected Reply saveReply(String commentId, String content, String userId, String pw) {
        SaveReplyInput replyInput = new SaveReplyInput();
        replyInput.setCommentId(commentId);
        replyInput.setContent(content);
        replyInput.setUserId(userId);
        replyInput.setPassword(pw);
        Reply reply = replyService.saveReply(replyInput).block();
        Assertions.assertThat(reply).isNotNull();
        return reply;
    }
}
