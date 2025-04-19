package com.example.demo.employee.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.employee.model.Employee;
import com.example.demo.employee.model.EmployeeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmpController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("employee/empLogin")
	public String empLogin() {
		return "employee/empLogin";
	}
	
	
	
	@GetMapping("employee/empIndex")
	public String empIndex() {
		return "employee/empIndex";
	}
	
	@GetMapping("employee/empRegister")
	public String empRegister() {
		return "employee/empRegister";
	}
	
	@PostMapping("employee/empRegister")
	public ResponseEntity<?> register(@ModelAttribute Employee employee){
		boolean result = employeeService.empExistCheck(employee);
		System.out.println(result);
		if (result) {
	        Employee insertEmployee = employeeService.insertEmployee(employee);
	        if (insertEmployee != null) {
	        	System.out.println("可以註冊");
	            return ResponseEntity.ok(Map.of(
	                "status", "success",
	                "message", "註冊成功",
	                "name", insertEmployee.getName(),
	                "email", insertEmployee.getEmail()
	            ));
	        } else {
	            return ResponseEntity
	                .badRequest()
	                .body(Map.of("status", "error", "message", "註冊失敗，新增失敗"));
	        }
	    } else {
	        return ResponseEntity
	            .badRequest()
	            .body(Map.of("status", "error", "message", "電子信箱或手機號碼已被使用"));
	    }
	}
	
	
	
	@PostMapping("/employee/empLogin")
	@ResponseBody
	public Map<String, Object> empLogin(@RequestParam("email") String email,
	                                 @RequestParam("password") String password,
	                                 HttpSession httpSession) {
	    Map<String, Object> response = new HashMap<>();
	    Employee LoginBean = employeeService.login(email, password);
	    if (LoginBean != null) {
	    	httpSession.setAttribute("empDetail", LoginBean);
	    	httpSession.setAttribute("empName", LoginBean.getName());
	    	httpSession.setAttribute("empEmail", LoginBean.getEmail());
	        response.put("status", "success");
	        response.put("empName", LoginBean.getName());
	    } else {
	        response.put("status", "error");
	        response.put("message", "帳號或密碼錯誤");
	    }
	    return response;
	}
	
	@GetMapping("employee/empLogout")
	public String empLogout(HttpSession httpSession) {
		httpSession.removeAttribute("empDetail");
    	httpSession.removeAttribute("empName");
    	httpSession.removeAttribute("empEmail");
		return "employee/empLogin";
	}

}
