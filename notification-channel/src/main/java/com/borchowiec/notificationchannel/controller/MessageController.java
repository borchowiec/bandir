package com.borchowiec.notificationchannel.controller;

import com.borchowiec.notificationchannel.handler.ReactiveWebSocketHandler;
import com.borchowiec.notificationchannel.model.WsMessage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final ReactiveWebSocketHandler webSocketHandler;

    public MessageController(ReactiveWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping("/{sessionId}")
    public void sendMessage(@PathVariable String sessionId, WsMessage message) {
        System.out.println("wsss");
        System.out.println(sessionId);
        webSocketHandler.sendMessage(sessionId, message);
    }
}
