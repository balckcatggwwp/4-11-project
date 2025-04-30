package com.example.demo.member.controller;

import com.example.demo.member.model.Member;
import com.example.demo.member.model.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/memberStatsAPI")
@RequiredArgsConstructor
public class MemberStatsController {

    private final MemberRepository memberRepository;

    @GetMapping("/detail")
    public Map<String, Object> getStats() {
        Map<String, Object> res = new HashMap<>();
        try {
            List<Member> all = memberRepository.findAll();

            int male = 0, female = 0, other = 0;
            int age1_14 = 0, age15_64 = 0, age65up = 0;
            int today = 0;
            LocalDate todayDate = LocalDate.now();

            for (Member m : all) {
                // 統計性別
                String gender = m.getGender();
                if ("male".equalsIgnoreCase(gender)) male++;
                else if ("female".equalsIgnoreCase(gender)) female++;
                else other++;

                // 統計年齡區間
                try {
                    LocalDate birth = LocalDate.parse(m.getDateOfBirth());
                    int age = Period.between(birth, todayDate).getYears();
                    if (age <= 14) age1_14++;
                    else if (age <= 64) age15_64++;
                    else age65up++;
                } catch (Exception e) {
                    // 日期格式錯誤略過
                }

                // 統計今日註冊
                if (m.getCreateTime() != null && m.getCreateTime().toLocalDate().equals(todayDate)) {
                    today++;
                }
            }

            res.put("status", "success");
            res.put("genderRatio", List.of(male, female, other));
            res.put("ageGroups", List.of(age1_14, age15_64, age65up));
            res.put("todayCount", today);
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }

        return res;
    }
}
