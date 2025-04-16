package com.example.graphqlfluxboard.comment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class SaveCommentInput {
    private String postId;
    private String userId;
    private String password;
    private String comment;
}
