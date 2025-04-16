package com.example.graphqlfluxboard.post.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SavePostInput {
    private String title;
    private String content;
    private String userId;
    private String password;
}
