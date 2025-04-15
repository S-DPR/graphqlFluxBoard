package com.example.graphqlfluxboard.comment.resolver;

import com.example.graphqlfluxboard.comment.domain.Comment;
import com.example.graphqlfluxboard.comment.dto.CommentInput;
import com.example.graphqlfluxboard.comment.service.CommentService;
import com.example.graphqlfluxboard.reply.domain.Reply;
import com.example.graphqlfluxboard.reply.sevice.ReplyService;
import com.example.graphqlfluxboard.user.domain.User;
import com.example.graphqlfluxboard.user.service.UserService;
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
    public Mono<Map<Comment, List<Reply>>> commentsWithReplies(List<Comment> comments) {
        Map<String, Comment> commentMap = comments.stream().collect(Collectors.toMap(Comment::getId, c -> c));

        return replyService.findAllByCommentIds(commentMap.keySet().stream().toList())
                .groupBy(Reply::getCommentId)
                .flatMap(groupedFlux -> groupedFlux
                        .collectSortedList(Comparator.comparing(Reply::getCreatedAt))
                        .map(list -> Map.entry(commentMap.get(groupedFlux.key()), list)))
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
