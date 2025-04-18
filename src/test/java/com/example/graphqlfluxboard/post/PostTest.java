package com.example.graphqlfluxboard.post;

import com.example.graphqlfluxboard.TestSupport;
import com.example.graphqlfluxboard.common.exception.impl.AuthException;
import com.example.graphqlfluxboard.common.exception.impl.NotFound;
import com.example.graphqlfluxboard.post.domain.Post;
import com.example.graphqlfluxboard.post.dto.DeletePostInput;
import com.example.graphqlfluxboard.post.dto.SavePostInput;
import com.example.graphqlfluxboard.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
public class PostTest extends TestSupport {
    @BeforeEach
    public void setup() {
        super.setUp();
    }

    @Test
    public void savePostWithWrongPW() {
        User user = saveUser("TEST_USER", TEST_PASSWORD);
        SavePostInput savePostInput = SavePostInput.builder()
                .title("TEST_POST")
                .userId(user.getId())
                .password(WRONG_PASSWORD)
                .content("CONTENT")
                .build();
        StepVerifier.create(postService.createPost(savePostInput))
                .expectError(AuthException.class)
                .verify();
    }

    @Test
    public void deletePostWithWrongPW() {
        User user = saveUser("TEST_USER", TEST_PASSWORD);
        Post post = savePost("TEST_POST", "CONTENT", user.getId(), TEST_PASSWORD);

        DeletePostInput deletePostInput = DeletePostInput.builder()
                .postId(post.getId())
                .password(WRONG_PASSWORD)
                .build();
        StepVerifier.create(postService.deletePost(deletePostInput))
                .expectError(AuthException.class)
                .verify();
        StepVerifier.create(postService.findPostById(post.getId()))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void deletePost() {
        User user = saveUser("TEST_USER", TEST_PASSWORD);
        Post post = savePost("TEST_POST", "CONTENT", user.getId(), TEST_PASSWORD);

        DeletePostInput deletePostInput = DeletePostInput.builder()
                .postId(post.getId())
                .password(TEST_PASSWORD)
                .build();
        StepVerifier.create(postService.deletePost(deletePostInput))
                .verifyComplete();
        StepVerifier.create(postService.findPostById(post.getId()))
                .expectError(NotFound.class)
                .verify();
    }
}
