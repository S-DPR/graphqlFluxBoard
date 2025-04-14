package com.example.graphqlfluxboard.board.reply.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyInput {
    private String commentId;
    private String authorName;
    private String password;
    private String content;
}
