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
    private String authorName;
    private String userId;
    private String password;
    private String comment;
}
