package com.borchowiec.user.service;

import com.borchowiec.user.client.NotificationClient;
import com.borchowiec.user.client.UserRepositoryClient;
import com.borchowiec.user.event.UserCreatedEvent;
import com.borchowiec.user.exception.AlreadyTakenException;
import com.borchowiec.user.model.User;
import com.borchowiec.user.model.WsMessage;
import com.borchowiec.user.payload.CreateUserRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;
    private final UserRepositoryClient userRepositoryClient;
    private final NotificationClient notificationClient;

    public UserService(PasswordEncoder passwordEncoder, ApplicationEventPublisher publisher,
                       UserRepositoryClient userRepositoryClient, NotificationClient notificationClient) {
        this.passwordEncoder = passwordEncoder;
        this.publisher = publisher;
        this.userRepositoryClient = userRepositoryClient;
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
        return userRepositoryClient
                .existsByUsername(request.getUsername())
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
        return userRepositoryClient
                .existsByEmail(request.getEmail())
                .doOnSuccess(emailAlreadyTaken -> {
                    if (emailAlreadyTaken) {
                        String message = String.format("Email '%s' already taken", request.getEmail());
                        WsMessage wsMessage = new WsMessage(WsMessage.MessageType.ERROR_MESSAGE, message);
                        notificationClient.sendMessage(wsMessage, wsSession).subscribe();
                        throw new AlreadyTakenException(message);
                    }
                });
    }

    public Mono<User> saveUser(CreateUserRequest request, String wsSession) {
        return checkIfUsernameIsAlreadyTaken(request, wsSession)
                .then(checkIfEmailIsAlreadyTaken(request, wsSession))
                .then(userRepositoryClient.save(toUser(request))
                        .doOnSuccess(entity -> publisher.publishEvent(new UserCreatedEvent(entity, wsSession))));
    }
}
