package com.borchowiec.auth.controller;

import com.borchowiec.auth.dto.AuthenticationRequest;
import com.borchowiec.auth.dto.AuthenticationToken;
import com.borchowiec.auth.exception.WrongCredentialsException;
import com.borchowiec.auth.model.AuthorizationRequest;
import com.borchowiec.auth.model.UserDetailsAuthentication;
import com.borchowiec.auth.model.UserDetailsImpl;
import com.borchowiec.auth.service.AuthService;
import com.borchowiec.remote.client.NotificationClient;
import com.borchowiec.remote.client.UserRepositoryClient;
import com.borchowiec.remote.model.Password;
import com.borchowiec.remote.model.User;
import com.borchowiec.remote.model.WsMessage;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.util.SimpleMethodInvocation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoader;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.UUID;

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
    public Password hashPassword(@RequestBody Password passwordDto) {
        String encoded = passwordEncoder.encode(passwordDto.getPassword());
        Password result = new Password();
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

    @PostMapping("/authorize")
    public Mono<ResponseEntity<?>> authorize(@RequestBody AuthorizationRequest request) {
        /*todo throw exceptions and returns different statuses e.g. 401 for user not found, 403 when user has no role*/
        return authService
                .authorize(request.getAuthToken(), request.getSecurityExpression())
                .map(authorized -> authorized
                        ? ResponseEntity.ok().build()
                        : ResponseEntity.status(HttpStatus.FORBIDDEN).build()
                );
    }
}
