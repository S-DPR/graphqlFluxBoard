package com.example.graphqlfluxboard.post.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PostInput {
    private String title;
    private String content;
    private String authorName;
    private String userId;
    private String password;
}
