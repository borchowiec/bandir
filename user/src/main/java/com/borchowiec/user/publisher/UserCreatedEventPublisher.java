package com.borchowiec.user.publisher;

import com.borchowiec.user.client.NotificationClient;
import com.borchowiec.user.event.UserCreatedEvent;
import com.borchowiec.user.model.User;
import com.borchowiec.user.model.WsMessage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventPublisher implements ApplicationListener<UserCreatedEvent> {
    private final NotificationClient notificationClient;

    public UserCreatedEventPublisher(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        User user = (User) event.getSource();

        String message = String.format("User %s has been created.", user.getUsername());
        WsMessage wsMessage = new WsMessage(WsMessage.MessageType.SUCCESS_MESSAGE, message);
        notificationClient.sendMessage(wsMessage, event.getWsSession()).subscribe();
    }
}
