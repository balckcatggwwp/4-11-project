package com.example.demo.member.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PasswordEncoder pwdEncoder;
	
	//Login Controller
	@GetMapping("member/login")
	public String loginPage() {
		return "member/login";
	}
	@PostMapping("/member/login")
	@ResponseBody
	public Map<String, Object> login(@RequestParam("email") String email,
	                                 @RequestParam("password") String password,
	                                 HttpSession httpSession) {
	    Map<String, Object> response = new HashMap<>();
	    Member LoginBean = memberService.login(email, password);
	    if (LoginBean != null) {
	    	httpSession.setAttribute("memberDetail", LoginBean);
	    	httpSession.setAttribute("memberName", LoginBean.getName());
	    	httpSession.setAttribute("memberEmail", LoginBean.getEmail());
	    	httpSession.setAttribute("memberId", LoginBean.getMemberId());
	        response.put("status", "success");
	        response.put("name", LoginBean.getName());
	    } else {
	        response.put("status", "error");
	        response.put("message", "帳號或密碼錯誤");
	    }
	    return response;
	}

	@GetMapping("member/logout")
	public String logout(HttpSession httpSession,RedirectAttributes redirectAttributes) {
		httpSession.removeAttribute("memberDetail");
	    httpSession.invalidate(); // 清除所有session
	    redirectAttributes.addFlashAttribute("logoutSuccess", true);
		return "redirect:/";

	}
	
	@GetMapping("member/register")
	public String register() {
		return "member/register";
	}
	
//前台會員註冊
	@PostMapping("member/register")
	public ResponseEntity<?> register(@ModelAttribute Member member){
		boolean result = memberService.memberExistCheck(member);
		
		if (result) {
	        Member insertMember = memberService.insertMember(member);
	        if (insertMember != null) {
	            return ResponseEntity.ok(Map.of(
	                "status", "success",
	                "message", "註冊成功",
	                "name", insertMember.getName(),
	                "email", insertMember.getEmail()
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
	
//	後臺編輯會員資訊
	
	//會員後臺首頁
	@GetMapping("member/MemberManager")
	public String MemberManager() {
		return "member/MemberManager";
	}
	
	//後臺新增會員
	@GetMapping("member/insertMember")
	public String insertMember() {
		return "member/insertMember";
	}
	
	//後臺顯示所有會員(DataTable)
	@GetMapping("member/showAllMember")
	public String showAllMember(Model model) {
	    List<Member> memberList = memberService.findAll();
	    model.addAttribute("members", memberList);
	    return "member/showAllMember";
	}
	
	//後臺編輯單筆會員資料(透過findById)
	@GetMapping("member/updateMember/{id}")
	public String updateMember(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
		Member member = memberService.findById(id);
		if (member!=null) {
			member.setPassword(null);
            model.addAttribute("member", member);
            return "member/updateMember"; // Return the name of your edit form template
        } else {
            // Handle case where member is not found
            redirectAttributes.addFlashAttribute("errorMessage", "找不到 ID 為 " + id + " 的會員。");
            return "redirect:/member/showAllMember";
        }
		
	}
	
	
	//後臺編輯單筆會員資料 儲存修改的資料
	@PostMapping("member/updateMember")
	public String updateMember(
			@ModelAttribute Member form,Model model){
		Long id = form.getMemberId();
		Member member = memberService.updateMember(id,form);
		if(member!=null) {
			model.addAttribute("message","修改成功");			
		}else {
			model.addAttribute("message","修改失敗");
		}
		return "member/updateMember";
	}
	
	//後臺透過指定欄位進行模糊搜尋，回傳List
	//	欄位搜尋
	@GetMapping("member/columnSearch")
	public String columnSearch() {
		return "member/columnSearch";
	}
	
	
	@PostMapping("member/columnSearch")
	public String showMember(String columnInfo, String columnName ,Model model) {
		System.out.println(columnInfo +" "+columnName);
		List<Member> members;
		switch (columnName) {
		case "email": {
			members = memberService.findByEmailContaining(columnInfo);
			break;
		}
		case "phoneNumber": {
			members = memberService.findByPhoneNumberContaining(columnInfo);
			break;
		}
		case "nationalId": {
			members = memberService.findByNationalIdContaining(columnInfo);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + columnName);
		}
		if (members!=null) {
			model.addAttribute("members", members);
			model.addAttribute("message","搜尋成功");
		}else {
			model.addAttribute("message","搜尋失敗");
		}
		return "/member/showAllMember";
		
	}
	
	@DeleteMapping("/member/deleteMember/{id}")
	@ResponseBody
	public ResponseEntity<String> deleteMember(@PathVariable Long id) {
	    boolean success = memberService.deleteMemberById(id);
	    if (success) {
	        return ResponseEntity.ok("刪除成功");
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("刪除失敗");
	    }
	}

	
	
	
//測試相關
	
	@GetMapping("layout/empNavbar")
	public String empNavbar() {
		return "layout/empNavbar";
	}
	
	@GetMapping("layout/empLayout")
	public String empLayout() {
		return "layout/empLayout";
	}
	
	@GetMapping("layout/navbar")
	public String navbar() {
		return "layout/navbar";
	}
	

	
	
	
	
	
	
	
}
