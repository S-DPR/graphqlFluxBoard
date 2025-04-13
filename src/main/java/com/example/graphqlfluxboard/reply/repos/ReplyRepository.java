package com.example.graphqlfluxboard.reply.repos;

import com.example.graphqlfluxboard.reply.domain.Reply;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Collection;

@Repository
public interface ReplyRepository extends ReactiveMongoRepository<Reply, String> {
    Flux<Reply> findByCommentIdIn(Collection<String> commentIds);
}
