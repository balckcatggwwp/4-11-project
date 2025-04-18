package com.example.demo.movieNews.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.demo.movieNews.model.ChatMessage;

@Controller
public class ChatController {

    @MessageMapping("/chat") // 客戶端發送到 /app/chat
    @SendTo("/topic/messages") // 廣播到 /topic/messages 給所有訂閱者
    public ChatMessage sendMessage(ChatMessage message) {
        return message; // 這邊可以加時間戳或進行儲存
    }
}