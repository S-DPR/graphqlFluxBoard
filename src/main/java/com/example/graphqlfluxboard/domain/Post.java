package com.example.graphqlfluxboard.domain;

import com.example.graphqlfluxboard.dto.PostInput;
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
    private LocalDateTime createdAt;

    public static Post of(PostInput postInput) {
        return Post.builder()
                .title(postInput.getTitle())
                .content(postInput.getContent())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
