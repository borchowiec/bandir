package com.borchowiec.user.publisher;

import com.borchowiec.user.event.UserCreatedEvent;
import com.borchowiec.user.handler.ReactiveWebSocketHandler;
import com.borchowiec.user.model.User;
import com.borchowiec.user.model.WsMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserCreatedEventPublisher implements ApplicationListener<UserCreatedEvent> {

    private final ReactiveWebSocketHandler webSocketHandler;

    @Value("${GATEWAY_ADDRESS}")
    private String gatewayUrl;

    public UserCreatedEventPublisher(ReactiveWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        User user = (User) event.getSource();

        String message = String.format("User %s has been created.", user.getUsername());
        WsMessage wsMessage = new WsMessage(WsMessage.MessageType.SUCCESS_MESSAGE, message);

        String url = String.format("/notification-channel/message/%s", event.getWsSession());
        System.out.println(url);

        WebClient.create(gatewayUrl)
                .post()
                .uri(url) // todo use gateway
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(wsMessage), WsMessage.class)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}
