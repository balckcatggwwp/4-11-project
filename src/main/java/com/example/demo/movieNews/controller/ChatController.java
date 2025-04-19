package com.example.demo.movieNews.controller;

<<<<<<< HEAD
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.demo.movieNews.model.ChatMessage;
=======
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
>>>>>>> master

@Controller
public class ChatController {

<<<<<<< HEAD
	@MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage message) {
        message.setContent(message.getSender() + " 加入聊天室");
        return message;
    }
}
=======
	  @Autowired
	    private ChatMessageRepository chatMessageRepository;

	    @MessageMapping("/chat")
	    @SendTo("/topic/messages")
	    public ChatMessage send(ChatMessage message, Principal principal, SimpMessageHeaderAccessor accessor) {

	    	 // 嘗試從 payload 拿
	        Long memberId = message.getMemberId();
	        String memberName = message.getSender();

	        // 如果 payload 沒有，就從 session 拿
	        if (memberId == null || memberName == null) {
	            memberId = (Long) accessor.getSessionAttributes().get("memberId");
	            memberName = (String) accessor.getSessionAttributes().get("memberName");
	        }

	        // 如果還是沒有，表示是非法訪問
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
>>>>>>> master
