package com.example.demo.employee.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTitleCategoryRepository extends JpaRepository<JobTitleCategory, Long> {
	
	List<JobTitleCategory> findByJobLevelLessThan(int loginLevel);
}
