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
	
	@GetMapping("employee/forgotPassword")
	public String forgotPassword() {
		return "employee/forgotPassword";
	}
	
	@GetMapping("memberCenter")
	public String memberCenter() {
		return "memberCenter";
	}
	
	@GetMapping("footer")
	public String footer() {
		return "fragments/footer";
	}
	
	@GetMapping("template")
	public String template() {
		return "memberCenterTemplate";
	}
	
	@GetMapping("navbar")
	public String navbar() {
		return "fragments/navbar";
	}
}
