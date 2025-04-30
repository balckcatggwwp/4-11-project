package com.example.demo.employee.model;

import lombok.Data;

@Data
public class EmployeeProfileEditDTO {
    private String name;
    private String gender;
    private String nationalId;
    private String email;
    private String phoneNumber;
    private String dateOfBirth;
    private String entryTime;
    private String password; // 可選（修改密碼時用）
}