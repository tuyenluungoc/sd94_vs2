package org.example.sd_94vs1.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Cấu hình message broker (dùng cho các destination bắt đầu bằng /topic)
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Broker cho tin nhắn tới "/topic/users"
        config.setApplicationDestinationPrefixes("/app"); // Prefix cho tin nhắn gửi đến server
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatus").withSockJS(); // Đặt endpoint cho kết nối WebSocket
    }
}