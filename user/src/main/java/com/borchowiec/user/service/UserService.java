package com.borchowiec.user.service;

import com.borchowiec.user.client.NotificationClient;
import com.borchowiec.user.event.UserCreatedEvent;
import com.borchowiec.user.exception.AlreadyTakenException;
import com.borchowiec.user.model.User;
import com.borchowiec.user.model.WsMessage;
import com.borchowiec.user.payload.CreateUserRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;
    private final WebClient webClient;
    private final NotificationClient notificationClient;

    public UserService(PasswordEncoder passwordEncoder, ApplicationEventPublisher publisher, WebClient webClient,
                       NotificationClient notificationClient) {
        this.passwordEncoder = passwordEncoder;
        this.publisher = publisher;
        this.webClient = webClient;
        this.notificationClient = notificationClient;
    }

    private User toUser(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return user;
    }

    private Mono<Boolean> checkIfUsernameIsAlreadyTaken(CreateUserRequest request, String wsSession) {
        return webClient
                .get()
                .uri("/user-repository/existsByUsername/{username}", request.getUsername())
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnSuccess(usernameAlreadyTaken -> {
                    if (usernameAlreadyTaken) {
                        String message = String.format("Username '%s' already taken", request.getUsername());
                        WsMessage wsMessage = new WsMessage(WsMessage.MessageType.ERROR_MESSAGE, message);
                        notificationClient.sendMessage(wsMessage, wsSession).subscribe();
                        throw new AlreadyTakenException(message);
                    }
                });
    }

    private Mono<Boolean> checkIfEmailIsAlreadyTaken(CreateUserRequest request, String wsSession) {
        return webClient
                .get()
                .uri("/user-repository/existsByEmail/{email}", request.getEmail())
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnSuccess(emailAlreadyTaken -> {
                    if (emailAlreadyTaken) {
                        String message = String.format("Email '%s' already taken", request.getEmail());
                        WsMessage wsMessage = new WsMessage(WsMessage.MessageType.ERROR_MESSAGE, message);
                        notificationClient.sendMessage(wsMessage, wsSession).subscribe();
                        throw new AlreadyTakenException(message);
                    }
                });
    }

    private Mono<User> saveUser(User user, String wsSession) {
        return webClient
                .post()
                .uri("/user-repository")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(User.class)
                .doOnSuccess(entity -> publisher.publishEvent(new UserCreatedEvent(entity, wsSession)));
    }

    public Mono<User> saveUser(CreateUserRequest request, String wsSession) {
        return checkIfUsernameIsAlreadyTaken(request, wsSession)
                .then(checkIfEmailIsAlreadyTaken(request, wsSession))
                .then(saveUser(toUser(request), wsSession));
    }
}
