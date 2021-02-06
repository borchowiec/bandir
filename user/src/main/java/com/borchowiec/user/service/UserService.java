package com.borchowiec.user.service;

import com.borchowiec.user.event.UserCreatedEvent;
import com.borchowiec.user.exception.AlreadyTakenException;
import com.borchowiec.user.model.User;
import com.borchowiec.user.payload.CreateUserRequest;
import com.borchowiec.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.publisher = publisher;
    }

    private User toUser(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return user;
    }

    public Mono<User> saveUser(CreateUserRequest request, String wsSession) {
        return userRepository
                .existsByUsername(request.getUsername())
                .doOnSuccess(usernameAlreadyTaken -> {
                    if (usernameAlreadyTaken) {
                        throw new AlreadyTakenException(String.format("Username '%s' already taken", request.getUsername()));
                    }
                })
                .then(userRepository.existsByEmail(request.getEmail()))
                .doOnSuccess(emailAlreadyTaken -> {
                    if (emailAlreadyTaken) {
                        throw new AlreadyTakenException(String.format("Email '%s' already taken", request.getUsername()));
                    }
                })
                .then(userRepository
                        .save(toUser(request))
                        .doOnSuccess(entity -> publisher.publishEvent(new UserCreatedEvent(entity, wsSession))));
    }
}
