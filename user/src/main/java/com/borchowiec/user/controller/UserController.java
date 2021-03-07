package com.borchowiec.user.controller;

import com.borchowiec.user.model.UserInfo;
import com.borchowiec.user.payload.CreateUserRequest;
import com.borchowiec.user.service.UserService;
import com.borchowiec.user.util.ValidationUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ValidationUtil validationUtil;

    public UserController(UserService userService, ValidationUtil validationUtil) {
        this.userService = userService;
        this.validationUtil = validationUtil;
    }

    @PostMapping
    public Mono<Void> addUser(@RequestBody CreateUserRequest request,
                              @RequestHeader("user-ws-session-id") String wsSession) {
        return Mono
                .fromRunnable(() -> validationUtil.validate(request, wsSession))
                .and(userService.saveUser(request, wsSession));
    }

    @GetMapping
    public Mono<UserInfo> getPrincipalInfo(@CookieValue(value = "Authorization", defaultValue = "") String authToken) {
        return userService.getPrincipalInfo(authToken.replace("%20", " "));
    }
}
