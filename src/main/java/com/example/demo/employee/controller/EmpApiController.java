package com.example.demo.employee.controller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.employee.model.Employee;
import com.example.demo.employee.model.EmployeeRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/employeeApi")
public class EmpApiController {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@GetMapping("/sessionInfo")
	public Map<String, Object> getSessionEmployee(HttpSession session) {
		UUID empId = (UUID) session.getAttribute("empId");
		if (empId == null)
			return Map.of("status", "error");

		Employee emp = employeeRepository.findById(empId).orElse(null);
		if (emp == null)
			return Map.of("status", "error");

		return Map.of("status", "success", "name", emp.getName(), "jobTitleName",
				emp.getJobTitleCategory().getJobTitleName(), "entryTime", emp.getEntryTime(), "gender", emp.getGender(),
				"phoneNumber", emp.getPhoneNumber());
	}
	@GetMapping("/sessionInfoForTopbar")
	public Map<String, Object> sessionInfoForTopbar(HttpSession session) {
		UUID empId = (UUID) session.getAttribute("empId");
		if (empId == null)
			return Map.of("status", "error");

		Employee emp = employeeRepository.findById(empId).orElse(null);
		if (emp == null)
			return Map.of("status", "error");

		return Map.of("status", "success", "name", emp.getName());
	}
}
