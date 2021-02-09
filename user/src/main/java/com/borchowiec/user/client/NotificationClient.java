package com.borchowiec.user.client;

import com.borchowiec.user.model.WsMessage;
import com.borchowiec.user.payload.CreateUserRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;

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

    public Mono<ResponseEntity<Void>> sendValidationErrorMessage(ConstraintViolation<CreateUserRequest> constraint,
                                                                 String wsSession) {
        String message = String.format("%s: %s", constraint.getPropertyPath(), constraint.getMessage());
        WsMessage wsMessage = new WsMessage(WsMessage.MessageType.ERROR_MESSAGE, message);

        return sendMessage(wsMessage, wsSession);
    }
}
