package com.example.graphqlfluxboard.board.comment.resolver;

import com.example.graphqlfluxboard.board.comment.domain.Comment;
import com.example.graphqlfluxboard.board.comment.dto.CommentInput;
import com.example.graphqlfluxboard.board.comment.service.CommentService;
import com.example.graphqlfluxboard.board.reply.domain.Reply;
import com.example.graphqlfluxboard.board.reply.sevice.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentResolver {
    private final CommentService commentService;
    private final ReplyService replyService;

    @QueryMapping
    public Flux<Comment> comments() {
        return commentService.getComments();
    }

    @QueryMapping
    public Mono<Comment> comment(@Argument String commentId) {
        return commentService.getComment(commentId);
    }

    @QueryMapping
    public Flux<Comment> commentsByPostId(@Argument String postId) {
        return commentService.getCommentByPostId(postId);
    }

    @BatchMapping(field = "replies", typeName = "Comment")
    public Mono<Map<String, List<Reply>>> commentsWithReplies(List<Comment> comments) {
        List<String> commentIds = comments.stream().map(Comment::getId).toList();

        return replyService.findAllByCommentIds(commentIds)
                .groupBy(Reply::getCommentId)
                .flatMap(groupedFlux -> groupedFlux
                        .collectSortedList(Comparator.comparing(Reply::getCreatedAt))
                        .map(list -> Map.entry(groupedFlux.key(), list)))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    @MutationMapping
    public Mono<Comment> createComment(@Argument CommentInput commentInput) {
        return commentService.saveComment(commentInput);
    }

    @MutationMapping
    public Mono<Boolean> deleteComment(@Argument String commentId, @Argument String password) {
        return commentService.deleteById(commentId, password).thenReturn(true);
    }
}
