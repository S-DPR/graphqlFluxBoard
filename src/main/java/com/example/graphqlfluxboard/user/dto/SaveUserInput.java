package com.example.graphqlfluxboard.user.dto;

import com.example.graphqlfluxboard.common.validation.DTOValidationMessage;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@ToString
@EqualsAndHashCode
public class SaveUserInput {
    @Size(min = 2, max = 10, message = DTOValidationMessage.USERNAME_SIZE_LIMIT)
    private String username;

    @Size(min = 4, message = DTOValidationMessage.PASSWORD_SIZE_LIMIT)
    private String password;
}
