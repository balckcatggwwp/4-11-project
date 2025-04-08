package com.example.demo.tick.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.tick.bean.BookticketBean;

public interface BooktickRepository extends JpaRepository<BookticketBean, Integer> {

    @Query("SELECT b FROM BookticketBean b WHERE b.hallid = :hallid AND b.showtimeid = :showtimeid")
    List<BookticketBean> findSeatidbyHallidShowtimeid(@Param("hallid") Integer hallid, @Param("showtimeid") Integer showtimeid);


}
