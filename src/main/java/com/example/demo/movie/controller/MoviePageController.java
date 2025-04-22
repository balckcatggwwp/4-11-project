package com.example.demo.movie.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MoviePageController {

	@GetMapping("/MovieList")
	public String ListPage() {
		return "/Movie";
	}
	
	@GetMapping("/showtime")
	public String ShowtimePage() {
		return "/Showtime";
	}
	@GetMapping("/ShowtimeSelect")
	public String ShowtimeSelectPage() {
		return "/ShowtimeSelect";
	}
	@GetMapping("/MovieShowing")
	public String MovieInfoPage() {
		return "/MovieShowing";
	}
	
	@GetMapping("/MovieInfo/{id}")
	public String getMovieDetails(@PathVariable String id, Model model) {
	    Integer ID = Integer.parseInt(id);
	    
	    return "/moviedetails"; // 返回顯示電影詳情的頁面
	}
}
