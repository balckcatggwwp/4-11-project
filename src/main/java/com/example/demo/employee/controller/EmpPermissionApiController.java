package com.example.demo.employee.controller;

import com.example.demo.employee.model.EmpPermissionCategory;
import com.example.demo.employee.model.EmpPermissionCategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/empPermissionApi")
@RequiredArgsConstructor
public class EmpPermissionApiController {

	private final EmpPermissionCategoryRepository empPermissionCategoryRepository;

	@PostMapping("/add")
	public Map<String, Object> addPermission(@RequestBody EmpPermissionCategory permission) {
		Map<String, Object> response = new HashMap<>();
		try {
			empPermissionCategoryRepository.save(permission);
			response.put("status", "success");
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}

	@GetMapping("/list")
	public List<EmpPermissionCategory> listPermissions() {
		return empPermissionCategoryRepository.findAll();
	}

	@DeleteMapping("/delete/{id}")
	public Map<String, Object> deletePermission(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			empPermissionCategoryRepository.deleteById(id);
			response.put("status", "success");
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}

	@PutMapping("/update/{id}")
	public Map<String, Object> updatePermission(@PathVariable Long id, @RequestBody EmpPermissionCategory updateData) {
		Map<String, Object> response = new HashMap<>();
		try {
			EmpPermissionCategory permission = empPermissionCategoryRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("權限不存在"));
			permission.setEmpPermissionTypeName(updateData.getEmpPermissionTypeName());
			empPermissionCategoryRepository.save(permission);
			response.put("status", "success");
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}
		return response;
	}

}
