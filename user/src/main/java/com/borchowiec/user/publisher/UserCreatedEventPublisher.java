package com.borchowiec.user.publisher;

import com.borchowiec.user.event.UserCreatedEvent;
import com.borchowiec.user.model.User;
import com.borchowiec.user.model.WsMessage;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserCreatedEventPublisher implements ApplicationListener<UserCreatedEvent> {
    private final WebClient webClient;

    public UserCreatedEventPublisher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        User user = (User) event.getSource();

        String message = String.format("User %s has been created.", user.getUsername());
        WsMessage wsMessage = new WsMessage(WsMessage.MessageType.SUCCESS_MESSAGE, message);

        String url = String.format("/notification-channel/message/%s", event.getWsSession());

        webClient
                .post()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(wsMessage)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}
