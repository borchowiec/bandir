package com.borchowiec.remote.client;

import com.borchowiec.remote.model.Password;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class AuthClient {
    private final WebClient webClient;

    public AuthClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Password> hashPassword(String password) {
        String url = "/auth/hash-password";
        return webClient
                .post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(Collections.singletonMap("password", password))
                .retrieve()
                .bodyToMono(Password.class);
    }
}
