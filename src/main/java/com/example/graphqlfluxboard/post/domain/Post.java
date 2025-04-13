package com.example.graphqlfluxboard.post.domain;

import com.example.graphqlfluxboard.post.dto.PostInput;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String authorName;
    private String password;
    private LocalDateTime createdAt;

    public static Post of(PostInput postInput, String hashedPassword) {
        return Post.builder()
                .title(postInput.getTitle())
                .content(postInput.getContent())
                .authorName(postInput.getAuthorName())
                .password(hashedPassword)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
