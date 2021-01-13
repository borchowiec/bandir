package com.borchowiec.user.controller;

import com.borchowiec.user.model.User;
import com.borchowiec.user.payload.CreateUserRequest;
import com.borchowiec.user.repository.UserRepository;
import com.borchowiec.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/user")
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @PostMapping("/user")
    public Mono<Void> addUser(@Validated @RequestBody CreateUserRequest request) {
        return userService.saveUser(request).then(); // todo send event via websockets
    }


}
