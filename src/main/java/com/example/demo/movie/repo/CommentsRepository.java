package com.example.demo.movie.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.movie.model.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {

	
	 List<Comments> findByMovieIdOrderByCreatedAtDesc(Integer movieId);
}
