package com.whatalk.chatservice.handler;

import com.whatalk.chatservice.Service.RedisService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomWebSocketHandler extends AbstractWebSocketHandler {

    private RedisService redisService;

//    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        // TODO: 2022/07/30 redis sub chat room
        // TODO: 2022/07/30 message listener
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        redisService.publishMessage(// chatroom, Message Object);
        // TODO: 2022/07/30 redis pub
    }



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    }
}
