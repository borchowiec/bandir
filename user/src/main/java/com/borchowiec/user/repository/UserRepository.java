package com.borchowiec.user.repository;

import com.borchowiec.user.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsByEmail(String email);
}
