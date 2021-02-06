package com.borchowiec.notificationchannel.handler;

import com.borchowiec.notificationchannel.model.WsMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {
    private final Map<String, WebSocketSession> sessionMap;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReactiveWebSocketHandler() {
        this.sessionMap = new HashMap<>();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        sessionMap.put(session.getId(), session);

        return Mono.empty()
                .and(session
                        .receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .doOnSubscribe(subscription -> {
                            WsMessage message = new WsMessage(WsMessage.MessageType.SESSION_ID, session.getId());
                            sendMessage(session, message);
                        })
                        .doOnComplete(() -> sessionMap.remove(session.getId()))
                        .log()
                );
    }

    public void sendMessage(WebSocketSession session, WsMessage message) {
        try {
            session
                    .send(Mono.just(objectMapper.writeValueAsString(message)).map(session::textMessage).log())
                    .subscribe();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String id, WsMessage message) {
        System.out.println(id);
        sendMessage(sessionMap.get(id), message);
    }
}