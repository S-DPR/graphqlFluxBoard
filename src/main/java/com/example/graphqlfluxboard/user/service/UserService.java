package com.example.graphqlfluxboard.user.service;

import com.example.graphqlfluxboard.reply.domain.Reply;
import com.example.graphqlfluxboard.user.domain.User;
import com.example.graphqlfluxboard.user.dto.UserInput;
import com.example.graphqlfluxboard.user.repos.UserRepository;
import com.example.graphqlfluxboard.utils.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Flux<User> findAllByIds(List<String> ids) {
        return userRepository.findAllByIdIn(ids);
    }

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    public Mono<User> save(UserInput userInput) {
        String password = passwordService.encryptPassword(userInput.getPassword());
        return save(User.of(userInput, password));
    }

    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }

    public Mono<Void> verify(String userId, String password) {
        return findById(userId)
                .filter(user -> passwordService.checkPassword(password, user.getPassword()))
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid password")))
                .then();
    }
}
