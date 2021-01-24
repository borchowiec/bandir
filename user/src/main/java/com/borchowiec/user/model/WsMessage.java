package com.borchowiec.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WsMessage {
    private final MessageType type;
    private final String payload;

    public enum MessageType {
        SUCCESS_MESSAGE, SESSION_ID
    }
}
