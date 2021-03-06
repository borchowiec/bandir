package com.borchowiec.userrepository.controller;

import com.borchowiec.userrepository.model.User;
import com.borchowiec.userrepository.repository.UserRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user-repository")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/getById/{id}")
    public Mono<User> getUserById(@PathVariable String id) {
        return userRepository.findById(id);
    }

    @GetMapping("/getByUsernameOrEmail/{usernameOrEmail}")
    public Mono<User> getUserByUsernameOrEmail(@PathVariable String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    @GetMapping("/existsByUsername/{username}")
    public Mono<Boolean> existsByUsername(@PathVariable String username) {
        return userRepository.existsByUsername(username);
    }

    @GetMapping("/existsByEmail/{email}")
    public Mono<Boolean> existsByEmail(@PathVariable String email) {
        return userRepository.existsByEmail(email);
    }

    @GetMapping
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @PostMapping
    public Mono<User> addUser(@Validated @RequestBody User user) {
        return userRepository.save(user);
    }
}
