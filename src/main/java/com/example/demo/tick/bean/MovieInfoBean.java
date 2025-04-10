package com.example.demo.tick.bean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity@Table(name = "Movieinfo")
public class MovieInfoBean {
	@Id@Column(name = "movieid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int movieid;
	@Column(name = "moviename")
	private String moviename;
	public String getMoviename() {
		return moviename;
	}
	public void setMoviename(String moviename) {
		this.moviename = moviename;
	}
	public int getMovieid() {
		return movieid;
	}
	public void setMovieid(int movieid) {
		this.movieid = movieid;
	}
}
