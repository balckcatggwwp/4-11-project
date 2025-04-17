package com.example.demo.movie.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.movie.model.MovieShowtime;

public interface MovieShowtimeRepository extends JpaRepository<MovieShowtime, Integer> {

}
