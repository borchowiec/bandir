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
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @PostMapping
    public void addUser(@Validated @RequestBody CreateUserRequest request) {
        Mono<User> userMono = userService.saveUser(request);
        userMono.block(); // todo send event via websockets
    }


}
