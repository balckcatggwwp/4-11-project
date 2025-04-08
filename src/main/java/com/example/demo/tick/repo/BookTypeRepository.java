package com.example.demo.tick.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.tick.bean.BookTypeBean;

public interface BookTypeRepository extends JpaRepository<BookTypeBean, Integer>{

}
