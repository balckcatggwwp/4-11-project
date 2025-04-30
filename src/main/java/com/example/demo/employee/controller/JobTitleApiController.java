package com.example.demo.employee.controller;

import com.example.demo.employee.model.JobTitleCategory;
import com.example.demo.employee.model.JobTitleCategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobTitleApi")
public class JobTitleApiController {

    private final JobTitleCategoryRepository jobTitleCategoryRepository;

    @PostMapping("/add")
    public Map<String, Object> addJobTitle(@RequestBody JobTitleCategory jobTitleCategory) {
        Map<String, Object> response = new HashMap<>();
        try {
            jobTitleCategoryRepository.save(jobTitleCategory);
            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }
    @GetMapping("/list")
    public List<JobTitleCategory> listJobTitles() {
        return jobTitleCategoryRepository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteJobTitle(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            jobTitleCategoryRepository.deleteById(id);
            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PutMapping("/update/{id}")
    public Map<String, Object> updateJobTitle(@PathVariable Long id, @RequestBody JobTitleCategory updateData) {
        Map<String, Object> response = new HashMap<>();
        try {
            JobTitleCategory existing = jobTitleCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("職稱不存在"));
            existing.setJobTitleName(updateData.getJobTitleName());
            existing.setJobLevel(updateData.getJobLevel());
            jobTitleCategoryRepository.save(existing);
            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }
}
