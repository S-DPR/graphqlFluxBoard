package com.example.graphqlfluxboard.user.domain;

import com.example.graphqlfluxboard.user.dto.SaveUserInput;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String username;
    private String password;

    public static User of(SaveUserInput saveUserInput, String password) {
        return User.builder()
                .username(saveUserInput.getUsername())
                .password(password)
                .build();
    }
}
