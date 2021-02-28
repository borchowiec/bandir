package com.borchowiec.auth.client;

import com.borchowiec.auth.model.WsMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NotificationClient {
    private final WebClient webClient;

    public NotificationClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ResponseEntity<Void>> sendMessage(WsMessage wsMessage, String wsSession) {
        String url = String.format("/notification-channel/message/%s", wsSession);
        return webClient
                .post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(wsMessage)
                .retrieve()
                .toBodilessEntity();
    }
}
