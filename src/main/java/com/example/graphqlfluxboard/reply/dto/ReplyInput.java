package com.example.graphqlfluxboard.reply.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyInput {
    private String id;
    private String commentId;
    private String userId;
    private String password;
    private String content;
}
