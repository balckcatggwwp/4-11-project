package com.example.demo.movie.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.movie.model.Comments;
import com.example.demo.movie.service.CommentsService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/movies/{movieId}/comments")
public class CommentsController {

    @Autowired
    private CommentsService commentService;

    @GetMapping
    public List<Comments> getComments(@PathVariable Integer movieId) {
        return commentService.getCommentsByMovieId(movieId);
    }

    @PostMapping
    public ResponseEntity<Void> postComment(@PathVariable Integer movieId,
                                            @RequestBody Map<String, String> body,
                                            HttpSession session) {
        Long memberid =  (Long) session.getAttribute("memberId");
        Integer memberId = Integer.valueOf(String.valueOf(memberid));
        String memberName = (String)session.getAttribute("memberName");
        
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String content = body.get("content");
        commentService.saveComment(movieId, memberId, memberName, content);
        return ResponseEntity.ok().build();
    }
    
    // 修改留言
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(
            @PathVariable Integer id,
            @RequestBody Map<String, String> payload,
            HttpSession session) {

        String memberName = (String) session.getAttribute("memberName");
        if (memberName == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登入");
        }

        String content = payload.get("content");
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("內容不能為空");
        }

        boolean updated = commentService.updateComment(id, memberName, content);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("無權限編輯他人留言");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Integer id,
            HttpSession session) {

        String memberName = (String) session.getAttribute("memberName");
        if (memberName == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登入");
        }

        boolean deleted = commentService.deleteComment(id, memberName);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("無權限刪除他人留言");
        }
    }
}

