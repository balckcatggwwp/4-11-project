package com.example.demo.member.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PasswordEncoder pwdEncoder;
	
	@Transactional
	public Member updateMember(Long id,Member updatedMember) {
		Optional<Member> optional = memberRepository.findById(id);
		if(optional.isPresent()) {
			Member originalMember = optional.get();					
			String newName  = updatedMember.getName();
			String newDateOfBirth  = updatedMember.getDateOfBirth();
			String newGender  = updatedMember.getGender();
		
		//更新資料
		originalMember.setName(newName);
		originalMember.setGender(newGender);
		originalMember.setDateOfBirth(newDateOfBirth);
		//更新資料 (密碼)
		if(updatedMember.getPassword() != null && !updatedMember.getPassword().isBlank()) {
			String newPassword  = updatedMember.getPassword();
			String hashedPwd = pwdEncoder.encode(newPassword);
			originalMember.setPassword(hashedPwd);
		}
			return originalMember;
		}else {
			return null;
		}
	}
	
	
	public Member insertMember(Member member) {
		String originalPwd =  member.getPassword();
		String hashedPwd = pwdEncoder.encode(originalPwd);
		member.setPassword(hashedPwd); 

		Member insertBean = memberRepository.save(member);
		
		
		
		
		return insertBean;
	}
	
	public boolean memberExistCheck(Member member) {
		String email = member.getEmail();
		String phoneNumber = member.getPhoneNumber();
		
		System.out.println(email + " "+phoneNumber);
		//檢查信箱&手機號碼是否已註冊
		List<Member> existMembers = memberRepository.findByEmailOrPhoneNumber(email,phoneNumber);
		if(!existMembers.isEmpty()) {
			//已存在回傳false 不給註冊
			return false;
		}else {
			//未存在回傳true 表示允許註冊
			return true;
		}
		
	}
	
	public Member login(String email,String originalPwd) {
		Member member = memberRepository.findByEmail(email);
		if(member!=null) {
			boolean result = pwdEncoder.matches(originalPwd, member.getPassword());
		
			if(result) {
				return member;
			}
		}
		return null;

	}
	public List<Member> findByPhoneNumberContaining(String phoneNumber) {
		List<Member> members = memberRepository.findByPhoneNumberContaining(phoneNumber);
		if (members!=null) {
			return members;
		}
		return null;
		
	}
	
	public List<Member> findByEmailContaining(String email) {
		List<Member> members = memberRepository.findByEmailContaining(email);
		if (members!=null) {
			return members;
		}
		return null;
		
	}
	
	public List<Member> findByNationalIdContaining(String nationalId) {
		List<Member> members = memberRepository.findByNationalIdContaining(nationalId);
		if (members!=null) {
			return members;
		}
		return null;
		
	}
	
	public Member findById(Long id) {
		Optional<Member> optional = memberRepository.findById(id);
		if (optional.isPresent()) {
			Member member = optional.get();
			return member;
		}
		return null;
		
	}
	
	public List<Member> findAll() {
		List<Member> members = memberRepository.findAll();
		if (!members.isEmpty()) {
			return members;
		}
		return null;
		
	}

	@Transactional
	public boolean deleteMemberById(Long id) {
		 if (memberRepository.existsById(id)) {
				memberRepository.deleteById(id);
				return true;
		    } else {
		        return false;
		    }
	}
	
	
}
