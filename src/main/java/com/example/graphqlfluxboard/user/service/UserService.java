package com.example.graphqlfluxboard.user.service;

import com.example.graphqlfluxboard.common.exception.impl.AuthException;
import com.example.graphqlfluxboard.common.exception.impl.DuplicateException;
import com.example.graphqlfluxboard.common.exception.enums.Resources;
import com.example.graphqlfluxboard.user.domain.User;
import com.example.graphqlfluxboard.user.dto.DeleteUserInput;
import com.example.graphqlfluxboard.user.dto.SaveUserInput;
import com.example.graphqlfluxboard.user.repos.UserRepository;
import com.example.graphqlfluxboard.common.validation.PasswordService;
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

    public Flux<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> findUserById(String id) {
        return userRepository.findById(id);
    }

    public Flux<User> findAllByIds(List<String> ids) {
        return userRepository.findAllByIdIn(ids);
    }

    public Mono<Boolean> existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    public Mono<User> createUser(SaveUserInput saveUserInput) {
        String password = passwordService.encryptPassword(saveUserInput.getPassword());
        return existsByUsername(saveUserInput.getUsername())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new DuplicateException(Resources.USERNAME));
                    }
                    return createUser(User.of(saveUserInput, password));
                });
    }

    public Mono<Void> deleteUser(String id) {
        return userRepository.deleteById(id);
    }

    public Mono<Void> deleteUser(DeleteUserInput deleteUserInput) {
        String id = deleteUserInput.getUserId();
        String password = deleteUserInput.getPassword();
        return findUserById(id)
                .flatMap(user -> verify(user.getId(), password))
                .then(deleteUser(id));
    }

    public Mono<Void> verify(String userId, String password) {
        Mono<User> userMono = findUserById(userId)
                .switchIfEmpty(Mono.error(new AuthException("없는 유저래요~")));
        return userMono.flatMap(user -> {
            if (!passwordService.checkPassword(password, user.getPassword())) {
                return Mono.error(new AuthException("비밀번호 에러래요~"));
            }
            return Mono.empty();
        });
    }
}
