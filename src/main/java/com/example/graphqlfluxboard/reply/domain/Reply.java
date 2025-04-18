package com.example.graphqlfluxboard.reply.domain;

import com.example.graphqlfluxboard.reply.dto.SaveReplyInput;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "reply")
public class Reply {
    @Id
    private String id;

    private String commentId;
    private String userId;
    private String content;
    private Instant createdAt;

    public static Reply of(SaveReplyInput saveReplyInput) {
        return Reply.builder()
                .commentId(saveReplyInput.getCommentId())
                .userId(saveReplyInput.getUserId())
                .content(saveReplyInput.getContent())
                .createdAt(Instant.now())
                .build();
    }
}
