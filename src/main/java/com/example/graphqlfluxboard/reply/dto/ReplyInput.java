package com.example.graphqlfluxboard.reply.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reply")
public class ReplyInput {
    @Id
    private String id;

    private String commentId;
    private String authorName;
    private String password;
    private String content;
    private LocalDateTime createdAt;
}
