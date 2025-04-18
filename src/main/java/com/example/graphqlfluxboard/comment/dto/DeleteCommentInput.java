package com.example.graphqlfluxboard.comment.dto;

import com.example.graphqlfluxboard.common.validation.DTOValidationMessage;
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
    @NotBlank(message = DTOValidationMessage.NOT_BLANK_COMMENT_ID)
    String commentId;

    @NotBlank(message = DTOValidationMessage.NOT_BLANK_PASSWORD)
    String password;
}
