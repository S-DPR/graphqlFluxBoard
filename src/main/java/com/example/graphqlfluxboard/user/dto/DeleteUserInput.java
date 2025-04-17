package com.example.graphqlfluxboard.user.dto;

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
public class DeleteUserInput {
    @NotBlank(message = DTOValidationMessage.NOT_BLANK_USER_ID)
    String userId;

    @NotBlank(message = DTOValidationMessage.NOT_BLANK_PASSWORD)
    String password;
}
