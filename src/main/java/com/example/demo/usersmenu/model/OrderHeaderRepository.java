package com.example.demo.usersmenu.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Integer> {
    List<OrderHeader> findByPhone(String phone);
    
    @Query("SELECT o FROM OrderHeader o LEFT JOIN FETCH o.items WHERE o.orderTime >= :startTime")
    List<OrderHeader> findRecentOrders(@Param("startTime") LocalDateTime startTime);
}
