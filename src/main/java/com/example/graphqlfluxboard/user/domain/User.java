package com.example.graphqlfluxboard.user.domain;

import com.example.graphqlfluxboard.user.dto.UserInput;
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

    public static User of(UserInput userInput, String password) {
        return User.builder()
                .username(userInput.getUsername())
                .password(password)
                .build();
    }
}
