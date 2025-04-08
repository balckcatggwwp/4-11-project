package com.example.demo.movie.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.movie.model.MovieList;
import com.example.demo.movie.repo.MovieListRepository;

@Service
public class MovieListService {

	@Autowired
	private MovieListRepository movieListRepository;
	
	public List<MovieList> findMoiveById(Integer id) {
		return movieListRepository.findAll();
	}
}
