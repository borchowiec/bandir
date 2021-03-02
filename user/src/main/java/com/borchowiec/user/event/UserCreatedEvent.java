package com.borchowiec.user.event;

import com.borchowiec.remote.model.User;
import org.springframework.context.ApplicationEvent;

public class UserCreatedEvent extends ApplicationEvent {
    private final String wsSession;

    public String getWsSession() {
        return wsSession;
    }

    public UserCreatedEvent(User source, String wsSession) {
        super(source);
        this.wsSession = wsSession;
    }
}
