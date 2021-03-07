package com.borchowiec.auth.service;

import com.borchowiec.auth.dto.AuthenticationToken;
import com.borchowiec.auth.exception.WrongCredentialsException;
import com.borchowiec.auth.model.UserDetailsAuthentication;
import com.borchowiec.auth.model.UserDetailsImpl;
import com.borchowiec.auth.util.SecurityUtil;
import com.borchowiec.remote.client.UserRepositoryClient;
import com.borchowiec.remote.model.User;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepositoryClient userRepositoryClient;

    public AuthService(PasswordEncoder passwordEncoder, JwtService jwtService,
                       UserRepositoryClient userRepositoryClient) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRepositoryClient = userRepositoryClient;
    }

    public Mono<AuthenticationToken> authenticateUser(User user, String password) {
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (!matches) {
            return Mono.error(new WrongCredentialsException());
        }

        return Mono.just(jwtService.getAuthenticationToken(user));
    }

    private boolean authorize(User user, String securityExpression) {
        UserDetails userDetails = new UserDetailsImpl(user);
        Authentication authentication = new UserDetailsAuthentication(userDetails, true);
        return SecurityUtil.checkExpression(authentication, securityExpression);
    }

    public Mono<Boolean> authorize(final String authToken, final String securityExpression) {
        try {
            return Mono
                    .just(jwtService.getClaims(authToken).getBody().getSubject())
                    .flatMap(userRepositoryClient::getById)
                    .map(user -> authorize(user, securityExpression))
                    .defaultIfEmpty(false);
        } catch (JwtException e) {
            return Mono.just(false);
        }
    }

    public Mono<User> getPrincipal(String authToken) {
        return Mono
                .just(jwtService.getClaims(authToken).getBody().getSubject())
                .flatMap(userRepositoryClient::getById)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
