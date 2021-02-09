package com.borchowiec.user.controller;

import com.borchowiec.user.model.User;
import com.borchowiec.user.model.WsMessage;
import com.borchowiec.user.payload.CreateUserRequest;
import com.borchowiec.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.*;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final WebClient webClient;
    private final Validator validator;

    public UserController(UserService userService, WebClient webClient, Validator validator) {
        this.userService = userService;
        this.webClient = webClient;
        this.validator = validator;
    }

    @GetMapping
    public Flux<User> findAll() {
        return webClient.get().uri("/user-repository").retrieve().bodyToFlux(User.class);
    }

    @PostMapping
    public Mono<Void> addUser(@RequestBody CreateUserRequest request,
                              @RequestHeader("user-ws-session-id") String wsSession) {
        return Mono.fromRunnable(() -> { // todo move it somewhere
            Set<ConstraintViolation<CreateUserRequest>> constraints = validator.validate(request);
            if (!constraints.isEmpty()) {
                constraints.forEach(constraint -> {
                    String message = String.format("%s: %s", constraint.getPropertyPath(), constraint.getMessage());
                    WsMessage wsMessage = new WsMessage(WsMessage.MessageType.ERROR_MESSAGE, message);
                    String url = String.format("/notification-channel/message/%s", wsSession);
                    webClient
                            .post()
                            .uri(url)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .bodyValue(wsMessage)
                            .retrieve()
                            .toBodilessEntity()
                            .subscribe();
                });
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, constraints.stream().findFirst().get().getMessage());
            }
        })
        .and(userService.saveUser(request, wsSession));
    }
}
