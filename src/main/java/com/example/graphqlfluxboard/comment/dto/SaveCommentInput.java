package com.example.graphqlfluxboard.comment.dto;

import com.example.graphqlfluxboard.common.validation.DTOValidationMessage;
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
    @NotBlank(message = DTOValidationMessage.NOT_BLANK_POST_ID)
    private String postId;

    @NotBlank(message = DTOValidationMessage.NOT_BLANK_USER_ID)
    private String userId;

    @NotBlank(message = DTOValidationMessage.NOT_BLANK_PASSWORD)
    private String password;

    @Size(min = 1, max = 1000, message = DTOValidationMessage.COMMENT_SIZE_LIMIT)
    private String comment;
}
