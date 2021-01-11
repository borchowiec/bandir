package com.borchowiec.user.controller;

import com.borchowiec.user.model.User;
import com.borchowiec.user.payload.CreateUserRequest;
import com.borchowiec.user.repository.UserRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    @PostMapping
    public void addUser(@Validated @RequestBody CreateUserRequest request) {
        System.out.println(request);
    }
}
