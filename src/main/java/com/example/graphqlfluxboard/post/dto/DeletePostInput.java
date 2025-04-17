package com.example.graphqlfluxboard.post.dto;

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
public class DeletePostInput {
    @NotBlank(message = DTOValidationMessage.NOT_BLANK_POST_ID)
    String postId;

    @NotBlank(message = DTOValidationMessage.NOT_BLANK_PASSWORD)
    String password;
}
