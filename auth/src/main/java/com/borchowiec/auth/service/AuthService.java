package com.borchowiec.auth.service;

import com.borchowiec.auth.dto.AuthenticationToken;
import com.borchowiec.auth.exception.WrongCredentialsException;
import com.borchowiec.remote.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Mono<AuthenticationToken> authenticateUser(User user, String password) {
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (!matches) {
            return Mono.error(new WrongCredentialsException());
        }

        return Mono.just(jwtService.getAuthenticationToken(user));
    }
}
