package com.example.graphqlfluxboard.service;

import com.example.graphqlfluxboard.domain.Post;
import com.example.graphqlfluxboard.dto.PostFilterInput;
import com.example.graphqlfluxboard.dto.PostInput;
import com.example.graphqlfluxboard.enums.FilterType;
import com.example.graphqlfluxboard.enums.SortOrder;
import com.example.graphqlfluxboard.repos.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.elasticsearch.ReactiveElasticsearchClientAutoConfiguration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("createdAt", "title", "authorName");
    private final PostRepository postRepository;
    private final ReactiveMongoTemplate mongoTemplate;
    private final PasswordEncoder passwordEncoder;

    public Mono<Post> findById(String postId) {
        return postRepository.findById(postId);
    }

    public Flux<Post> findAll(PostFilterInput postFilterInput) {
        if (!ALLOWED_SORT_FIELDS.contains(postFilterInput.getSortField())) {
            throw new RuntimeException("sort field is not supported");
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

    public Mono<Post> save(Post post) {
        return postRepository.save(post);
    }

    public Mono<Post> save(PostInput postInput) {
        String password = passwordEncoder.encode("sa" + postInput.getPassword() + "lt");
        return save(Post.of(postInput, password));
    }

    public Mono<Void> deleteById(String id) {
        return postRepository.deleteById(id);
    }

    public Mono<Void> deleteById(String id, String password) {
        return findById(id)
                .flatMap(post -> {
                    if (checkPassword(post.getPassword(), password)) {
                        return postRepository.delete(post);
                    }
                    return Mono.error(new RuntimeException("Invalid password"));
                });
    }

    public boolean checkPassword(String raw, String hashed) {
        return passwordEncoder.matches("sa" + raw + "lt", hashed);
    }
}
