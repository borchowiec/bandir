package com.borchowiec.auth.client;

import com.borchowiec.auth.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserRepositoryClient {
    private final WebClient webClient;

    public UserRepositoryClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<User> getByUsernameOrEmail(String usernameOrEmail) {
        return webClient
                .get()
                .uri("/user-repository/getByUsernameOrEmail/{usernameOrEmail}", usernameOrEmail)
                .retrieve()
                .bodyToMono(User.class);
    }
}
