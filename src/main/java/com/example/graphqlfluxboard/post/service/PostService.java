package com.example.graphqlfluxboard.post.service;

import com.example.graphqlfluxboard.common.exception.NotSupport;
import com.example.graphqlfluxboard.post.domain.Post;
import com.example.graphqlfluxboard.post.dto.PostFilterInput;
import com.example.graphqlfluxboard.post.dto.PostInput;
import com.example.graphqlfluxboard.post.enums.FilterType;
import com.example.graphqlfluxboard.post.enums.SortOrder;
import com.example.graphqlfluxboard.post.repos.PostRepository;
import com.example.graphqlfluxboard.user.service.UserService;
import com.example.graphqlfluxboard.utils.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("createdAt", "title", "authorName");
    private final PostRepository postRepository;
    private final ReactiveMongoTemplate mongoTemplate;
    private final UserService userService;

    public Mono<Post> findById(String postId) {
        return postRepository.findById(postId)
                .switchIfEmpty(Mono.error(new NotSupport(postId + ": 이 게시글 못찾았대요~")));
    }

    public Flux<Post> findAll(PostFilterInput postFilterInput) {
        if (!ALLOWED_SORT_FIELDS.contains(postFilterInput.getSortField())) {
            throw new NotSupport("다음 필드는 정렬을 지원하지 않습니다. : " + postFilterInput.getSortField());
        }

        List<Criteria> criteria = getCriteria(postFilterInput.getType(), postFilterInput.getKeyword());
        Query query = new Query(new Criteria().andOperator(criteria));
        query.skip((long) postFilterInput.getPage() * postFilterInput.getSizePerPage());
        query.limit(postFilterInput.getSizePerPage());

        Sort.Direction direction = Sort.Direction.DESC;
        if (postFilterInput.getSortOrder() == SortOrder.ASC) {
            direction = Sort.Direction.ASC;
        }
        query.with(Sort.by(direction, postFilterInput.getSortField()));
        return mongoTemplate.find(query, Post.class);
    }

    // Criteria는 처음써보는데 신기하네
    private List<Criteria> getCriteria(FilterType filterType, String keyword) {
        if (filterType == null || keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }
        return switch (filterType) {
            case NONE -> Collections.emptyList();
            case AUTHOR -> List.of(Criteria.where("authorName").regex(keyword, "i"));
            case TITLE -> List.of(Criteria.where("title").regex(keyword, "i"));
            case CONTENT -> List.of(Criteria.where("content").regex(keyword, "i"));
            case TITLE_AND_CONTENT -> List.of(new Criteria().orOperator(
                    Criteria.where("title").regex(keyword, "i"),
                    Criteria.where("content").regex(keyword, "i")
            ));
        };
    }

    public Mono<Boolean> existsById(String postId) {
        return postRepository.existsById(postId);
    }

    public Mono<Post> save(Post post) {
        return postRepository.save(post);
    }

    public Mono<Post> save(PostInput postInput) {
        return userService.verify(postInput.getUserId(), postInput.getPassword())
                .then(save(Post.of(postInput)));
    }

    public Mono<Void> deleteById(String id) {
        return postRepository.deleteById(id);
    }

    public Mono<Void> deleteById(String id, String password) {
        return findById(id)
                .flatMap(post -> userService.verify(post.getUserId(), password))
                .then(deleteById(id));
    }
}
