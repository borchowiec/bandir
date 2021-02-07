package com.borchowiec.user.controller;

import com.borchowiec.user.model.User;
import com.borchowiec.user.payload.CreateUserRequest;
import com.borchowiec.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final WebClient webClient;

    public UserController(UserService userService, WebClient webClient) {
        this.userService = userService;
        this.webClient = webClient;
    }

    @GetMapping
    public Flux<User> findAll() {
        return webClient.get().uri("/user-repository").retrieve().bodyToFlux(User.class);
    }

    @PostMapping
    public Mono<Void> addUser(@Validated @RequestBody CreateUserRequest request,
                              @RequestHeader("user-ws-session-id") String wsSession) {
        return userService.saveUser(request, wsSession).then();
    }
}
