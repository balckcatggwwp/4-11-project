package com.example.demo.movie.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public void saveComment(Integer movieId, Integer memberId, String memberName, String content) {
        Comments comment = new Comments();
        comment.setMovieId(movieId);
        comment.setMemberId(memberId);
        comment.setMemberName(memberName);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        commentsRepository.save(comment);
    }

    public boolean updateComment(Integer id, String memberName, String newContent) {
        Optional<Comments> optionalComment = commentsRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comments comment = optionalComment.get();
            if (comment.getMemberName().equals(memberName)) {
                comment.setContent(newContent);
                commentsRepository.save(comment);
                return true;
            }
        }
        return false;
    }
    
    public boolean deleteComment(Integer id, String memberName) {
        Optional<Comments> optionalComment = commentsRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comments comment = optionalComment.get();
            if (comment.getMemberName().equals(memberName)) {
            	commentsRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }
}
