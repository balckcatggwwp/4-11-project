package com.example.demo.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.member.model.MemberType;
import com.example.demo.member.model.MemberTypeRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/memberTypeApi")
@RequiredArgsConstructor
public class MemberTypeApiController {

    private final MemberTypeRepository memberTypeRepository;

    @GetMapping("/list")
    public List<MemberType> listAll() {
        return memberTypeRepository.findAll();
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody MemberType type) {
        Map<String, Object> res = new HashMap<>();
        try {
            memberTypeRepository.save(type);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }

    @PutMapping("/update/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody MemberType updateData) {
        Map<String, Object> res = new HashMap<>();
        try {
            MemberType mt = memberTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("ID不存在"));
            mt.setTypeName(updateData.getTypeName());
            mt.setApplicationFee(updateData.getApplicationFee());
            memberTypeRepository.save(mt);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        Map<String, Object> res = new HashMap<>();
        try {
            memberTypeRepository.deleteById(id);
            res.put("status", "success");
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }
        return res;
    }
}