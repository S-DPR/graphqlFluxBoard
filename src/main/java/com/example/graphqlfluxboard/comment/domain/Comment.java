package com.example.graphqlfluxboard.comment.domain;

import com.example.graphqlfluxboard.comment.dto.SaveCommentInput;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "comment")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private String id;

    private String postId;
    private String userId;
    private String comment;
    private Instant createdAt;

    public static Comment of(SaveCommentInput saveCommentInput) {
        return Comment.builder()
                .comment(saveCommentInput.getComment())
                .userId(saveCommentInput.getUserId())
                .postId(saveCommentInput.getPostId())
                .createdAt(Instant.now())
                .build();
    }
}
