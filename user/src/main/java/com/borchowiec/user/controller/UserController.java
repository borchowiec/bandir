package com.borchowiec.user.controller;

import com.borchowiec.user.model.User;
import com.borchowiec.user.payload.CreateUserRequest;
import com.borchowiec.user.service.UserService;
import com.borchowiec.user.util.ValidationUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final WebClient webClient;
    private final ValidationUtil validationUtil;

    public UserController(UserService userService, WebClient webClient, ValidationUtil validationUtil) {
        this.userService = userService;
        this.webClient = webClient;
        this.validationUtil = validationUtil;
    }

    @GetMapping
    public Flux<User> findAll() {
        return webClient.get().uri("/user-repository").retrieve().bodyToFlux(User.class);
    }

    @PostMapping
    public Mono<Void> addUser(@RequestBody CreateUserRequest request,
                              @RequestHeader("user-ws-session-id") String wsSession) {
        return Mono
                .fromRunnable(() -> validationUtil.validate(request, wsSession))
                .and(userService.saveUser(request, wsSession));
    }
}
