package com.example.demo.member.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	//Login Controller
	@GetMapping("/login")
	public String loginPage() {
		return "member/login";
	}
	@PostMapping("/login")
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

	@GetMapping("/logout")
	public String logout(HttpSession httpSession,RedirectAttributes redirectAttributes) {
		httpSession.removeAttribute("memberDetail");
	    httpSession.invalidate(); // 清除所有session
	    redirectAttributes.addFlashAttribute("logoutSuccess", true);
		return "redirect:/";

	}
	
	@GetMapping("/register")
	public String register() {
		return "member/register";
	}
	
//前台會員註冊
	@PostMapping("/register")
	public ResponseEntity<?> register(@ModelAttribute Member member){
		boolean result = memberService.memberExistCheck(member);
		
		if (result) {
			String verifyCode = UUID.randomUUID().toString();
	        Member insertMember = memberService.insertMember(member,verifyCode);
	        String Toemail = insertMember.getEmail();
	        String siteURL = "http://localhost:8080";
	        String verifyLink = siteURL + "/member/verify?code=" + verifyCode;
	        System.out.println(Toemail+" "+verifyLink);
	        try {
				memberService.sendVerificationEmail(Toemail,verifyLink);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
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
	@GetMapping("/MemberManager")
	public String MemberManager() {
		return "member/MemberManager";
	}
	
	//後臺新增會員
	@GetMapping("/insertMember")
	public String insertMember() {
		return "member/insertMember";
	}
	@GetMapping("member/showAllMember")
	public String showAllMember2(Model model) {
	    List<Member> memberList = memberService.findAll();
	    model.addAttribute("members", memberList);
	    return "member/showAllMember";
	}
	
	
	
	//後臺顯示所有會員(DataTable)
	@GetMapping("/showAllMember3")
	public String showAllMember(Model model) {
	    List<Member> memberList = memberService.findAll();
	    model.addAttribute("members", memberList);
	    return "member/showAllMember3";
	}
	
	//後臺編輯單筆會員資料(透過findById)
	@GetMapping("/updateMember/{id}")
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
	@PostMapping("/updateMember")
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
	@GetMapping("/columnSearch")
	public String columnSearch() {
		return "member/columnSearch";
	}
	
	
	@PostMapping("/columnSearch")
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
	
	@DeleteMapping("/deleteMember/{id}")
	@ResponseBody
	public ResponseEntity<String> deleteMember(@PathVariable Long id) {
	    boolean success = memberService.deleteMemberById(id);
	    if (success) {
	        return ResponseEntity.ok("刪除成功");
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("刪除失敗");
	    }
	}

	
	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("code") String code, Model model) {
	    String result = memberService.verifyMessage(code);

	    switch (result) {
	        case "認證成功" -> {
	            model.addAttribute("message", "帳號已成功啟用，請登入！");
	            return "member/verifySuccess";
	        }
	        case "已認證過了" -> {
	            model.addAttribute("message", "您已經完成過認證，請直接登入。");
	            return "member/verifySuccess";
	        }
	        case "錯誤" -> {
	            model.addAttribute("message", "驗證失敗，連結可能已過期或無效！");
	            return "member/verifyFail";
	        }
	        default -> {
	            model.addAttribute("message", "發生未知錯誤！");
	            return "member/verifyFail";
	        }
	    }
	}


}
