package com.example.graphqlfluxboard.controller;

import com.example.graphqlfluxboard.domain.Post;
import com.example.graphqlfluxboard.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

//    @GetMapping("/")
//    public Flux<Post> findAll() {
//        return postService.findAll();
//    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Post>> findById(@PathVariable String id) {
        return postService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public Mono<Post> save(@RequestBody Post post) {
        post.setCreatedAt(LocalDateTime.now());
        return postService.save(post);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return postService.deleteById(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
