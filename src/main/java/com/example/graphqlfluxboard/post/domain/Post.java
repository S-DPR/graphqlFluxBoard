package com.example.graphqlfluxboard.post.domain;

import com.example.graphqlfluxboard.post.dto.SavePostInput;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@Document(collection = "posts")
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    private String id;

    private String title;
    private String content;
    private String userId;
    private Instant createdAt;

    public static Post of(SavePostInput savePostInput) {
        return Post.builder()
                .title(savePostInput.getTitle())
                .content(savePostInput.getContent())
                .userId(savePostInput.getUserId())
                .createdAt(Instant.now())
                .build();
    }
}
