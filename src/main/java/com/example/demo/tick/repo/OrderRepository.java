package com.example.demo.tick.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.tick.bean.OrderBean;

public interface OrderRepository extends JpaRepository<OrderBean, Long> {

	 List<OrderBean> findByUserid(Long userid);
}
