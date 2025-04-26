package com.example.demo.movie.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.movie.model.Comments;
import com.example.demo.movie.repo.CommentsRepository;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    public List<Comments> getCommentsByMovieId(Integer movieId) {
        return commentsRepository.findByMovieIdOrderByCreatedAtDesc(movieId);
    }

    public void saveComment(Integer movieId, Integer memberId, String content) {
        Comments comment = new Comments();
        comment.setMovieId(movieId);
        comment.setMemberId(memberId);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        commentsRepository.save(comment);
    }
}