package com.example.demo.movie.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.movie.model.ViewData;

public interface ViewDataRepository extends JpaRepository<ViewData, Integer> {

}
