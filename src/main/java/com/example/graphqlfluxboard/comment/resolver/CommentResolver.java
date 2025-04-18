package com.example.graphqlfluxboard.comment.resolver;

import com.example.graphqlfluxboard.comment.domain.Comment;
import com.example.graphqlfluxboard.comment.dto.DeleteCommentInput;
import com.example.graphqlfluxboard.comment.dto.SaveCommentInput;
import com.example.graphqlfluxboard.comment.service.CommentService;
import com.example.graphqlfluxboard.reply.domain.Reply;
import com.example.graphqlfluxboard.reply.sevice.ReplyService;
import com.example.graphqlfluxboard.user.domain.User;
import com.example.graphqlfluxboard.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentResolver {
    private final CommentService commentService;
    private final ReplyService replyService;
    private final UserService userService;

    @QueryMapping
    public Flux<Comment> findAllComments() {
        return commentService.findAllComments();
    }

    @QueryMapping
    public Mono<Comment> findCommentById(@Argument String commentId) {
        return commentService.findCommentById(commentId);
    }

    @QueryMapping
    public Flux<Comment> commentsByPostId(@Argument String postId) {
        return commentService.findCommentByPostId(postId);
    }

    @MutationMapping
    public Mono<Comment> createComment(@Valid @Argument SaveCommentInput saveCommentInput) {
        return commentService.createComment(saveCommentInput);
    }

    @MutationMapping
    public Mono<Boolean> deleteComment(@Valid @Argument DeleteCommentInput deleteCommentInput) {
        return commentService.deleteComment(deleteCommentInput).thenReturn(true);
    }

    @BatchMapping(field = "replies", typeName = "Comment")
    public Mono<Map<Comment, List<Reply>>> commentsWithReplies(List<Comment> comments) {
        Map<String, Comment> commentMap = comments.stream().collect(Collectors.toMap(Comment::getId, c -> c));

        return replyService.findAllRepliesByCommentIds(commentMap.keySet().stream().toList())
                .groupBy(Reply::getCommentId)
                .flatMap(groupedFlux -> groupedFlux
                        .collectSortedList(Comparator.comparing(Reply::getCreatedAt))
                        .map(list -> Map.entry(commentMap.get(groupedFlux.key()), list)))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    @BatchMapping(field = "user", typeName = "Comment")
    public Mono<Map<Comment, User>> getUsers(List<Comment> comments) {
        List<String> userIds = comments.stream().map(Comment::getUserId).distinct().toList();
        return userService.findAllByIds(userIds)
                .collectMap(User::getId)
                .map(userMap -> {
                    return comments.stream().collect(Collectors.toMap(comment -> comment, comment -> userMap.get(comment.getUserId())));
                });
    }
}
