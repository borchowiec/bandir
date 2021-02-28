package com.borchowiec.auth.controller;

import com.borchowiec.auth.client.NotificationClient;
import com.borchowiec.auth.client.UserRepositoryClient;
import com.borchowiec.auth.dto.AuthenticationRequest;
import com.borchowiec.auth.dto.AuthenticationToken;
import com.borchowiec.auth.dto.PasswordDto;
import com.borchowiec.auth.exception.WrongCredentialsException;
import com.borchowiec.auth.model.WsMessage;
import com.borchowiec.auth.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryClient userRepositoryClient;
    private final AuthService authService;
    private final NotificationClient notificationClient;

    public AuthController(PasswordEncoder passwordEncoder, UserRepositoryClient userRepositoryClient,
                          AuthService authService, NotificationClient notificationClient) {
        this.passwordEncoder = passwordEncoder;
        this.userRepositoryClient = userRepositoryClient;
        this.authService = authService;
        this.notificationClient = notificationClient;
    }

    @PostMapping("/hash-password")
    public PasswordDto hashPassword(@RequestBody PasswordDto passwordDto) {
        String encoded = passwordEncoder.encode(passwordDto.getPassword());
        PasswordDto result = new PasswordDto();
        result.setPassword(encoded);
        return result;
    }

    @PostMapping("/authenticate")
    public Mono<AuthenticationToken> authenticate(@RequestBody AuthenticationRequest request,
                                                  @RequestHeader("user-ws-session-id") String wsSession) {
        return userRepositoryClient
                .getByUsernameOrEmail(request.getUsernameOrEmail())
                .switchIfEmpty(Mono.error(new WrongCredentialsException()))
                .flatMap(user -> authService.authenticateUser(user, request.getPassword()))
                .doOnError(throwable -> {
                    WsMessage message = new WsMessage(WsMessage.MessageType.ERROR_MESSAGE, throwable.getMessage());
                    notificationClient.sendMessage(message, wsSession).subscribe();
                });
    }
}
