package com.example.demo.tick.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.tick.bean.ShowtimeBean;

public interface ShowtimeRepository extends JpaRepository<ShowtimeBean, Integer> {

	List<ShowtimeBean> findStarttimeByStartdate(String startdate);
}
