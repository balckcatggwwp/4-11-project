package com.example.demo.movieNews.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.movieNews.model.ChatMessage;
import com.example.demo.movieNews.model.ChatMessageRepository;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @GetMapping("/history/{memberId}")
    public List<ChatMessage> getHistory(@PathVariable Long memberId) {
        return chatMessageRepository.findByMemberIdOrderByTimestampAsc(memberId);
    }
}
