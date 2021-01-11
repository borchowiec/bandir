package com.borchowiec.user.service;

import com.borchowiec.user.exception.AlreadyTakenException;
import com.borchowiec.user.model.User;
import com.borchowiec.user.payload.CreateUserRequest;
import com.borchowiec.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private User toUser(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return user;
    }

    public Mono<User> saveUser(CreateUserRequest request) {
        Boolean usernameAlreadyTaken = userRepository
                .existsByUsername(request.getUsername())
                .toProcessor()
                .block();

        if (usernameAlreadyTaken) {
            throw new AlreadyTakenException(String.format("Username '%s' already taken", request.getUsername()));
        }

        Boolean emailAlreadyTaken = userRepository
                .existsByEmail(request.getEmail())
                .toProcessor()
                .block();
        if (emailAlreadyTaken) {
            throw new AlreadyTakenException(String.format("Email '%s' already taken", request.getEmail()));
        }

        User user = toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }
}
