package com.example.demo.employee.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.employee.model.EmpPermissionCategory;
import com.example.demo.employee.model.EmpPermissionCategoryRepository;
import com.example.demo.employee.model.Employee;
import com.example.demo.employee.model.EmployeeAddDTO;
import com.example.demo.employee.model.EmployeeRepository;
import com.example.demo.employee.model.JobTitleCategory;
import com.example.demo.employee.model.JobTitleCategoryRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/employeeApi")
public class EmpApiController {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JobTitleCategoryRepository jobTitleCategoryRepository;

	@Autowired
	private EmpPermissionCategoryRepository empPermissionCategoryRepository;

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

	@GetMapping("/currentInfo")
	public Map<String, Object> getCurrentEmployeeInfo(HttpSession session) {
		UUID empId = (UUID) session.getAttribute("empId");
		if (empId == null) {
			return Map.of("error", "尚未登入");
		}

		Employee emp = employeeRepository.findById(empId).orElse(null);
		if (emp == null || emp.getJobTitleCategory() == null) {
			return Map.of("error", "員工資料異常");
		}

		Map<String, Object> data = new HashMap<>();
		data.put("empId", emp.getEmpId());
		data.put("jobLevel", emp.getJobTitleCategory().getJobLevel());

		List<Long> permissionIds = emp.getPermissions().stream().map(EmpPermissionCategory::getEmpPermissionTypeId)
				.collect(Collectors.toList());

		data.put("permissionIds", permissionIds);
		return data;
	}

	/**
	 * 新增員工（限制只能指派比自己低職等、且只分配自己擁有的權限）
	 */
	@PostMapping("/add")
	public Map<String, Object> addEmployee(@RequestBody EmployeeAddDTO dto, HttpSession session) {
		Map<String, Object> res = new HashMap<>();

		UUID currentEmpId = (UUID) session.getAttribute("empId");
		Employee currentEmp = employeeRepository.findById(currentEmpId).orElse(null);
		if (currentEmp == null) {
			return Map.of("status", "error", "message", "登入已過期，請重新登入");
		}

		int currentLevel = currentEmp.getJobTitleCategory().getJobLevel();
		List<Long> currentPermIds = currentEmp.getPermissions().stream()
				.map(EmpPermissionCategory::getEmpPermissionTypeId).toList();

		try {
			// 檢查職稱
			JobTitleCategory job = jobTitleCategoryRepository.findById(dto.getJobTitleCategoryId())
					.orElseThrow(() -> new RuntimeException("職稱不存在"));
			if (job.getJobLevel() >= currentLevel) {
				return Map.of("status", "error", "message", "只能指派比自己職等低的職稱");
			}

			// 查權限
			List<Long> requestedPermIds = dto.getPermissionIds();
			List<EmpPermissionCategory> perms = empPermissionCategoryRepository.findAllById(requestedPermIds);

			// 防呆：檢查是否全部權限都有查到（防止傳無效 ID）
			if (perms.size() != requestedPermIds.size()) {
				return Map.of("status", "error", "message", "部分權限 ID 無效或不存在");
			}

			// 驗證是否都是自己擁有的權限
			for (EmpPermissionCategory p : perms) {
				if (!currentPermIds.contains(p.getEmpPermissionTypeId())) {
					return Map.of("status", "error", "message", "不可指派不屬於自己的權限：" + p.getEmpPermissionTypeName());
				}
			}

			// 建立員工
			Employee emp = new Employee();
			emp.setName(dto.getName());
			emp.setGender(dto.getGender());
			emp.setNationalId(dto.getNationalId());
			emp.setDateOfBirth(dto.getDateOfBirth());
			emp.setEntryTime(dto.getEntryTime());
			emp.setEmail(dto.getEmail());
			emp.setPhoneNumber(dto.getPhoneNumber());
			emp.setPassword(passwordEncoder.encode(dto.getPassword()));
			emp.setJobTitleCategory(job);
			emp.setPermissions(new HashSet<>(perms));

			employeeRepository.save(emp);
			res.put("status", "success");

		} catch (Exception e) {
			res.put("status", "error");
			res.put("message", e.getMessage());
		}

		return res;
	}

	@GetMapping("/listLower")
	public List<Map<String, Object>> listLowerEmployees(HttpSession session) {
		UUID empId = (UUID) session.getAttribute("empId");
		if (empId == null) {
			return List.of(); // 尚未登入
		}

		Employee currentEmp = employeeRepository.findById(empId).orElse(null);
		if (currentEmp == null || currentEmp.getJobTitleCategory() == null) {
			return List.of(); // 資料異常
		}

		int currentLevel = currentEmp.getJobTitleCategory().getJobLevel();

		return employeeRepository.findAll().stream()
				.filter(e -> e.getJobTitleCategory() != null && e.getJobTitleCategory().getJobLevel() < currentLevel)
				.map(e -> {
					Map<String, Object> map = new HashMap<>();
					map.put("empId", e.getEmpId());
					map.put("name", e.getName());
					map.put("gender", e.getGender());
					map.put("phoneNumber", e.getPhoneNumber());
					map.put("email", e.getEmail());
					map.put("jobTitleName", e.getJobTitleCategory().getJobTitleName());
					return map;
				}).collect(Collectors.toList());
	}

	@GetMapping("/detail/{empId}")
	public Map<String, Object> getEmployeeDetail(@PathVariable UUID empId) {
		Employee emp = employeeRepository.findById(empId).orElseThrow(() -> new RuntimeException("找不到員工"));

		Map<String, Object> res = new HashMap<>();
		res.put("empId", emp.getEmpId());
		res.put("name", emp.getName());
		res.put("gender", emp.getGender());
		res.put("phoneNumber", emp.getPhoneNumber());
		res.put("email", emp.getEmail());
		res.put("nationalId", emp.getNationalId());
		res.put("dateOfBirth", emp.getDateOfBirth());
		res.put("entryTime", emp.getEntryTime());
		res.put("jobTitleName", emp.getJobTitleCategory().getJobTitleName());
		res.put("jobTitleCategoryId", emp.getJobTitleCategory().getJobTitleCategoryId());

		// 權限名稱清單（for 編輯打勾）
		List<String> permNames = emp.getPermissions().stream().map(EmpPermissionCategory::getEmpPermissionTypeName)
				.collect(Collectors.toList());
		res.put("permissions", permNames);

		return res;
	}

	@GetMapping("/permissions/{empId}")
	public List<String> getPermissions(@PathVariable UUID empId) {
		Employee emp = employeeRepository.findById(empId).orElseThrow(() -> new RuntimeException("找不到員工"));
		return emp.getPermissions().stream().map(EmpPermissionCategory::getEmpPermissionTypeName)
				.collect(Collectors.toList());
	}

	@DeleteMapping("/delete/{empId}")
	public Map<String, Object> deleteEmployee(@PathVariable UUID empId) {
		Map<String, Object> res = new HashMap<>();
		try {
			employeeRepository.deleteById(empId);
			res.put("status", "success");
		} catch (Exception e) {
			res.put("status", "error");
			res.put("message", e.getMessage());
		}
		return res;
	}

	@PutMapping("/update")
	public Map<String, Object> updateEmployee(@RequestParam UUID empId, @RequestParam String name,
			@RequestParam String gender, @RequestParam String nationalId, @RequestParam String dateOfBirth,
			@RequestParam String entryTime, @RequestParam String email, @RequestParam String phoneNumber,
			@RequestParam(required = false) String password, @RequestParam Long jobTitleCategoryId,
			@RequestParam List<Long> permissionIds) {
		Map<String, Object> res = new HashMap<>();

		try {
			Employee emp = employeeRepository.findById(empId).orElseThrow(() -> new RuntimeException("員工不存在"));

			emp.setName(name);
			emp.setGender(gender);
			emp.setNationalId(nationalId);
			emp.setDateOfBirth(dateOfBirth);
			emp.setEntryTime(entryTime);
			emp.setEmail(email);
			emp.setPhoneNumber(phoneNumber);

			if (password != null && !password.isBlank()) {
				emp.setPassword(passwordEncoder.encode(password));
			}

			JobTitleCategory jobTitle = jobTitleCategoryRepository.findById(jobTitleCategoryId)
					.orElseThrow(() -> new RuntimeException("職稱不存在"));
			emp.setJobTitleCategory(jobTitle);

			List<EmpPermissionCategory> perms = empPermissionCategoryRepository.findAllById(permissionIds);
			emp.setPermissions(new HashSet<>(perms));

			employeeRepository.save(emp);
			res.put("status", "success");

		} catch (Exception e) {
			res.put("status", "error");
			res.put("message", e.getMessage());
		}

		return res;
	}

}
