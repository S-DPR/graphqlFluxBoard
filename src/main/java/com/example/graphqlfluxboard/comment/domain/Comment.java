package com.example.graphqlfluxboard.comment.domain;

import com.example.graphqlfluxboard.comment.dto.CommentInput;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String parentCommentId; // 답글이 아닌 댓글일 경우 빈 값
    private String authorName;
    private String password;
    private String comment;
    private LocalDateTime createdAt;

    public static Comment of(CommentInput commentInput, String password) {
        return Comment.builder()
                .comment(commentInput.getComment())
                .authorName(commentInput.getAuthorName())
                .parentCommentId(commentInput.getParentCommentId())
                .password(password)
                .postId(commentInput.getPostId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
