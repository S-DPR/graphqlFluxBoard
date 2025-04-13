package com.example.graphqlfluxboard.comment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class CommentInput {
    private String postId;
    private String parentCommentId; // 답글이 아닌 댓글일 경우 빈 값
    private String authorName;
    private String password;
    private String comment;
}
