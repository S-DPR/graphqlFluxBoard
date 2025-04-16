package com.example.graphqlfluxboard.user.dto;

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
    private String username;
    private String password;
}
