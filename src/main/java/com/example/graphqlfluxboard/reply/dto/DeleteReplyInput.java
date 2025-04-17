package com.example.graphqlfluxboard.reply.dto;

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
public class DeleteReplyInput {
    @NotBlank(message = DTOValidationMessage.NOT_BLANK_REPLY_ID)
    String replyId;

    @NotBlank(message = DTOValidationMessage.NOT_BLANK_PASSWORD)
    String password;
}
