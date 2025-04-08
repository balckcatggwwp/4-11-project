package com.example.demo.movie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.movie.model.MovieList;
import com.example.demo.movie.service.MovieListService;

@RestController
public class MovieListController {

	@Autowired
	private MovieListService movieListService;
	
	@GetMapping("/t3.controller")
	public List<MovieList> test() {
		return movieListService.findMoiveById(1);
	}
}
