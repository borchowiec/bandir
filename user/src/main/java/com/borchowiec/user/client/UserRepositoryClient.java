package com.borchowiec.user.client;

import com.borchowiec.user.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserRepositoryClient {
    private final WebClient webClient;

    public UserRepositoryClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Boolean> existsByUsername(String username) {
        return webClient
                .get()
                .uri("/user-repository/existsByUsername/{username}", username)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public Mono<Boolean> existsByEmail(String email) {
        return webClient
                .get()
                .uri("/user-repository/existsByEmail/{email}", email)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public Mono<User> save(User user) {
        return webClient
                .post()
                .uri("/user-repository")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(User.class);
    }
}
