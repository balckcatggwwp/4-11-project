package com.example.demo.tick.bean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity@Table(name = "halls")
public class HallsBean {
	@Id @Column(name = "hallid")
	private int hallid;
	public int getHallid() {
		return hallid;
	}
	public void setHallid(int hallid) {
		this.hallid = hallid;
	}
}
