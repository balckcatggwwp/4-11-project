package com.example.demo.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
	
	@GetMapping("employee/empLogin2")
	public String empLogin2() {
		return "employee/empLogin2";
	}
	
	@GetMapping("employee/empRegister2")
	public String empRegister2() {
		return "employee/empRegister2";
	}
	
	@GetMapping("employee/forgot-password")
	public String forgotPassword() {
		return "employee/forgot-password";
	}
}
