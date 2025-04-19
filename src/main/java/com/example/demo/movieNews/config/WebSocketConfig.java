package com.example.demo.movieNews.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
<<<<<<< HEAD
=======
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
>>>>>>> master

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
<<<<<<< HEAD
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	config.enableSimpleBroker("/topic", "/queue"); // queue 是點對點用
        config.setUserDestinationPrefix("/user");      // 用 /user 為私訊起點
        config.setApplicationDestinationPrefixes("/app");
=======
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 客服訊息會傳到這
        config.setApplicationDestinationPrefixes("/app"); // 前端送出訊息會加這個 prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").addInterceptors(new WebSocketSessionInterceptor()).setAllowedOriginPatterns("*").withSockJS(); // 提供連線端點
>>>>>>> master
    }
}
