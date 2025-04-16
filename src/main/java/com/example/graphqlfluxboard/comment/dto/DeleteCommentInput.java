package com.example.graphqlfluxboard.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class DeleteCommentInput {
    @NotBlank(message = "Comment ID는 필수입니다.")
    String commentId;

    @NotBlank(message = "Password는 필수입니다.")
    String password;
}
