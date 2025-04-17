package com.example.graphqlfluxboard.post.dto;

import com.example.graphqlfluxboard.common.validation.DTOValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SavePostInput {
    @Size(min = 1, max = 100, message = DTOValidationMessage.TITLE_SIZE_LIMIT)
    private String title;

    @Size(min = 1, max = 50000, message = DTOValidationMessage.CONTENT_SIZE_LIMIT)
    private String content;

    @NotBlank(message = DTOValidationMessage.NOT_BLANK_USER_ID)
    private String userId;

    @NotBlank(message = DTOValidationMessage.NOT_BLANK_PASSWORD)
    private String password;
}
