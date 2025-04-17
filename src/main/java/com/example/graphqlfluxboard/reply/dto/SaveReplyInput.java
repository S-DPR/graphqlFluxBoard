package com.example.graphqlfluxboard.reply.dto;

import com.example.graphqlfluxboard.common.validation.DTOValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveReplyInput {
    @NotBlank(message = DTOValidationMessage.NOT_BLANK_COMMENT_ID)
    private String commentId;

    @NotBlank(message = DTOValidationMessage.NOT_BLANK_USER_ID)
    private String userId;

    @NotBlank(message = DTOValidationMessage.NOT_BLANK_PASSWORD)
    private String password;

    @Size(min = 1, max = 1000, message = DTOValidationMessage.COMMENT_SIZE_LIMIT)
    private String content;
}
