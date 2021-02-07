package com.borchowiec.notificationchannel.controller;

import com.borchowiec.notificationchannel.handler.ReactiveWebSocketHandler;
import com.borchowiec.notificationchannel.model.WsMessage;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final ReactiveWebSocketHandler webSocketHandler;

    public MessageController(ReactiveWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping("/{sessionId}")
    public void sendMessage(@PathVariable String sessionId, @RequestBody WsMessage message) {
        webSocketHandler.sendMessage(sessionId, message);
    }
}
