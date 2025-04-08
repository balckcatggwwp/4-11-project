package com.example.demo.tick.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.tick.bean.onofflineBean;
import java.util.List;


public interface onoffRepository extends JpaRepository<onofflineBean, Integer> {

	onofflineBean  findMovienameByHallid(Integer hallid);
}
