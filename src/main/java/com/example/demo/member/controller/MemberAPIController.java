package com.example.demo.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberDTOFE;
import com.example.demo.member.model.MemberRepository;
import com.example.demo.member.model.MemberService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/memberAPI")
public class MemberAPIController {
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
    private MemberService memberService;
	
	@Autowired
	private PasswordEncoder pwdEncoder;


	 // 1. 讀取會員資料
    @GetMapping("/memberDetial/{memberId}")
    public MemberDTOFE getMemberDetail(@PathVariable Long memberId) {
        Member member = memberService.findById(memberId);
        return new MemberDTOFE(
            member.getMemberId(),
            member.getName(),
            member.getGender(),
            member.getEmail(),
            member.getPhoneNumber(),
            member.getNationalId(),
            member.getDateOfBirth() != null ? member.getDateOfBirth().toString() : null,
            member.getMemberType().getTypeName()
        );
    }

    // 2. 更新會員資料
    @PostMapping("/updateProfile")
    public Map<String, Object> updateMemberProfile(
            @RequestParam Long memberId,
            @RequestParam String name,
            @RequestParam String dateOfBirth,
            @RequestParam String gender,
            @RequestParam String nationalId
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            memberService.updateMemberProfile(memberId, name, dateOfBirth, gender,nationalId);
            response.put("status", "success");
            response.put("message", "會員資料更新成功！");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "更新失敗：" + e.getMessage());
        }
        return response;
    }
    @PostMapping("/changePassword")
    public Map<String, Object> changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmNewPassword,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        Member loginMember = (Member) session.getAttribute("memberDetail");

        if (loginMember == null) {
            response.put("status", "error");
            response.put("message", "請先登入！");
            return response;
        }

        // 1. 驗證原密碼是否正確
        if (!pwdEncoder.matches(currentPassword, loginMember.getPassword())) {
            response.put("status", "error");
            response.put("message", "原密碼錯誤！");
            return response;
        }

        // 2. 驗證新密碼不能跟原密碼一樣
        if (pwdEncoder.matches(newPassword, loginMember.getPassword())) {
            response.put("status", "error");
            response.put("message", "新密碼不能與原密碼相同！");
            return response;
        }

        // 3. 驗證新密碼與確認新密碼一致
        if (!newPassword.equals(confirmNewPassword)) {
            response.put("status", "error");
            response.put("message", "新密碼與確認新密碼不一致！");
            return response;
        }

        // 4. 更新密碼
        loginMember.setPassword(pwdEncoder.encode(newPassword));
        memberRepository.save(loginMember);

        response.put("status", "success");
        response.put("message", "密碼變更成功！");
        return response;
    }
    
    

}

