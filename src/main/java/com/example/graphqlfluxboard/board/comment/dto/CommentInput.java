package com.example.graphqlfluxboard.board.comment.dto;

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
    private String password;
    private String comment;
}
