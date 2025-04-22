package com.example.demo.movie.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.movie.model.MovieList;

public interface MovieListRepository extends JpaRepository<MovieList, Integer> {
	
	List<MovieList> findByState(String state); 
	

	
	 @Query(value = """
		        SELECT m.id, m.name, d.description, d.image
		        FROM movielist m
		        JOIN moviedetail d ON m.id = d.id
		        WHERE m.state = '上映'
		    """, nativeQuery = true)
		    List<Map<String, Object>> findNowShowingMovies();
	

}
