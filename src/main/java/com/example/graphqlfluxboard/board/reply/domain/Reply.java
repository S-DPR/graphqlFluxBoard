package com.example.graphqlfluxboard.board.reply.domain;

import com.example.graphqlfluxboard.board.reply.dto.ReplyInput;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String authorName;
    private String password;
    private String content;
    private LocalDateTime createdAt;

    public static Reply of(ReplyInput replyInput, String password) {
        return Reply.builder()
                .authorName(replyInput.getAuthorName())
                .commentId(replyInput.getCommentId())
                .password(password)
                .content(replyInput.getContent())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
