package com.borchowiec.user.publisher;

import com.borchowiec.user.event.UserCreatedEvent;
import com.borchowiec.user.handler.ReactiveWebSocketHandler;
import com.borchowiec.user.model.User;
import com.borchowiec.user.model.WsMessage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventPublisher implements ApplicationListener<UserCreatedEvent> {

    private final ReactiveWebSocketHandler webSocketHandler;

    public UserCreatedEventPublisher(ReactiveWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        System.out.println("event publisher");
        User user = (User) event.getSource();
        String message = String.format("User %s has been created.", user.getUsername());
        WsMessage wsMessage = new WsMessage(WsMessage.MessageType.SUCCESS_MESSAGE, message);
        webSocketHandler.sendMessage(event.getWsSession(), wsMessage); // todo sessions id
    }
}
