package com.example.graphqlfluxboard.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class SaveCommentInput {
    @NotBlank(message = "PostID는 필수입니다.")
    private String postId;

    @NotBlank(message = "UserID는 필수입니다.")
    private String userId;

    @NotBlank(message = "Password는 필수입니다.")
    private String password;

    @Size(min = 1, max = 1000, message = "Comment는 1자 이상 1000자 이하로 작성해야 합니다.")
    private String comment;
}
