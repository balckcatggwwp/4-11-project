package com.example.demo.employee.model;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	private EmpPermissionCategoryRepository empPermissionCategoryRepository;

	@Autowired
	private JobTitleCategoryRepository jobTitleCategoryRepository;

	@Transactional
	public Employee insertEmployee(Employee emp) {
		// 加密密碼
		emp.setPassword(pwdEncoder.encode(emp.getPassword()));

		// 若前端只傳 ID，則必須在此查出關聯物件
		Long jobTitleId = emp.getJobTitleCategory().getJobTitleCategoryId();
		JobTitleCategory job = jobTitleCategoryRepository.findById(jobTitleId)
				.orElseThrow(() -> new RuntimeException("職稱不存在 ID: " + jobTitleId));
		emp.setJobTitleCategory(job);

		// 權限處理
		Set<EmpPermissionCategory> permSet = new HashSet<>();
		for (EmpPermissionCategory p : emp.getPermissions()) {
			Long pid = p.getEmpPermissionTypeId();
			EmpPermissionCategory perm = empPermissionCategoryRepository.findById(pid)
					.orElseThrow(() -> new RuntimeException("權限不存在 ID: " + pid));
			permSet.add(perm);
		}
		emp.setPermissions(permSet);

		return employeeRepository.save(emp);
	}

	public Employee login(String email, String originalPwd) {
		Employee employee = employeeRepository.findByEmail(email);
		if (employee != null) {
			boolean result = pwdEncoder.matches(originalPwd, employee.getPassword());

			if (result) {
				return employee;
			}
		}
		return null;

	}

	public Employee findById(UUID id) {
		Optional<Employee> optional = employeeRepository.findById(id);
		if (optional.isPresent()) {
			Employee employee = optional.get();
			return employee;
		}
		return null;

	}

	public boolean empExistCheck(Employee employee) {
		String email = employee.getEmail();
		String phoneNumber = employee.getPhoneNumber();

//		System.out.println(email + " "+phoneNumber);
		// 檢查信箱&手機號碼是否已註冊
		List<Employee> existEmployees = employeeRepository.findByEmailOrPhoneNumber(email, phoneNumber);
		if (!existEmployees.isEmpty()) {
			System.out.println("已被註冊");
			// 已存在回傳false 不給註冊
			return false;
		} else {
			// 未存在回傳true 表示允許註冊
			System.out.println("可以註冊");
			return true;
		}
	}

	// 註冊管理員帳號
	@Transactional
	public void registerAdmin(AdminRegisterDTO dto) {
		Employee emp = new Employee();
		emp.setName(dto.getName());
		emp.setGender(dto.getGender());
		emp.setNationalId(dto.getNationalId());
		emp.setDateOfBirth(dto.getDateOfBirth());
		emp.setEntryTime(dto.getEntryTime());
		emp.setEmail(dto.getEmail());
		emp.setPhoneNumber(dto.getPhoneNumber());
		emp.setPassword(pwdEncoder.encode(dto.getPassword()));

		JobTitleCategory job = jobTitleCategoryRepository.findById(dto.getJobTitleCategoryId())
				.orElseThrow(() -> new RuntimeException("職稱不存在"));
		emp.setJobTitleCategory(job);

		Set<EmpPermissionCategory> perms = new HashSet<>();
		for (Long pid : dto.getPermissionIds()) {
			EmpPermissionCategory p = empPermissionCategoryRepository.findById(pid)
					.orElseThrow(() -> new RuntimeException("權限不存在 ID: " + pid));
			perms.add(p);
		}
		emp.setPermissions(perms);

		employeeRepository.save(emp);
	}

	public boolean checkEmailOrPhoneAvailable(String email, String phoneNumber) {
		List<Employee> existEmployees = employeeRepository.findByEmailOrPhoneNumber(email, phoneNumber);
		return existEmployees.isEmpty(); // 空的就表示可用
	}
}
