package com.borchowiec.auth.controller;

import com.borchowiec.auth.client.UserRepositoryClient;
import com.borchowiec.auth.dto.AuthenticationRequest;
import com.borchowiec.auth.dto.AuthenticationToken;
import com.borchowiec.auth.dto.PasswordDto;
import com.borchowiec.auth.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepositoryClient userRepositoryClient;

    public AuthController(PasswordEncoder passwordEncoder, JwtService jwtService,
                          UserRepositoryClient userRepositoryClient) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userRepositoryClient = userRepositoryClient;
    }

    @PostMapping("/hash-password")
    public PasswordDto hashPassword(@RequestBody PasswordDto passwordDto) {
        String encoded = passwordEncoder.encode(passwordDto.getPassword());
        PasswordDto result = new PasswordDto();
        result.setPassword(encoded);
        return result;
    }

    @PostMapping("/authenticate")
    public Mono<AuthenticationToken> authenticate(@RequestBody AuthenticationRequest request) {
        return userRepositoryClient.getByUsernameOrEmail(request.getUsernameOrEmail())
                .flatMap(user -> {
                    // todo move this to service class
                    // todo doesnt throw error when user doesnt exists
                    if (user == null) {
                        return Mono.error(new RuntimeException("Not found username")); // todo return 401
                    }

                    boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
                    if (!matches) {
                        return Mono.error(new RuntimeException("Wrong pass")); // todo return 401
                    }

                    return Mono.just(jwtService.getAuthenticationToken(user));
                });
    }
}
