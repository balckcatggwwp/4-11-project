package com.example.demo.movieNews.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.example.demo.movieNews.model.ChatMessage;
import com.example.demo.movieNews.model.ChatMessageRepository;

@Controller
public class ChatController {

	  @Autowired
	    private ChatMessageRepository chatMessageRepository;

	    @MessageMapping("/chat")
	    @SendTo("/topic/messages")
	    public ChatMessage send(ChatMessage message, Principal principal, SimpMessageHeaderAccessor accessor) {

	    	// 預設從 session 取得
	        Long sessionMemberId = (Long) accessor.getSessionAttributes().get("memberId");
	        String sessionMemberName = (String) accessor.getSessionAttributes().get("memberName");

	        // 先從 payload 取，如果是 null 才 fallback
	        Long memberId = (message.getMemberId() != null) ? message.getMemberId() : sessionMemberId;
	        String memberName = (message.getSender() != null) ? message.getSender() : sessionMemberName;

	        if (memberId == null || memberName == null) {
	            System.out.println("❌ 無法識別發送者！");
	            throw new IllegalArgumentException("memberId 為必填");
	        }

	        message.setMemberId(memberId);
	        message.setSender(memberName);
	        message.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

	        chatMessageRepository.save(message);

	        return message;
	    }
}