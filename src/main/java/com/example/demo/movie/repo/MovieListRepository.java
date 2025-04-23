package com.example.demo.movie.repo;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
	 
	  @Query(value = """
		        SELECT 
		            m.name AS name,
		            m.released AS released,
		            m.runtime AS runtime,
		            d.rating AS rating,
		            d.genre AS genre,
		            d.director AS director,
		            d.actor AS actor,
		            d.description AS description,
		            d.image AS image
		        FROM movielist m
		        JOIN moviedetail d ON m.id = d.id
		        WHERE m.id = :id
		        """, nativeQuery = true)
		    Map<String, Object> findMovieInfo(@Param("id") Integer id);
	

}
