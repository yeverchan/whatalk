package com.whatalk.chatservice.config;

import com.whatalk.chatservice.handler.CustomTextWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CustomTextWebSocketHandler customTextWebSocketHandler;

    public WebSocketConfig(CustomTextWebSocketHandler customTextWebSocketHandler) {
        this.customTextWebSocketHandler = customTextWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(customTextWebSocketHandler, "/chats/ws")
                .setAllowedOrigins("*");
    }
}
