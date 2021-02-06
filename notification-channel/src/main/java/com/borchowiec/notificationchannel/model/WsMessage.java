package com.borchowiec.notificationchannel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WsMessage {
    private final MessageType type;
    private final String payload;

    public enum MessageType {
        SUCCESS_MESSAGE, ERROR_MESSAGE, ALERT_MESSAGE, SESSION_ID
    }
}
