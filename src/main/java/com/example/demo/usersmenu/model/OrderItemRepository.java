package com.example.demo.usersmenu.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {



	@Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.menu WHERE oi.order = :order")
	List<OrderItem> findItemsWithMenuByOrder(@Param("order") OrderHeader order);

	 List<OrderItem> findByOrderOrderId(Integer orderId);
}
