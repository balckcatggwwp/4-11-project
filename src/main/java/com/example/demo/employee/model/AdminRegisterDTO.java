package com.example.demo.employee.model;

import java.util.List;

import lombok.Data;

@Data
public class AdminRegisterDTO {
	private String name;
    private String gender;
    private String nationalId;
    private String dateOfBirth;
    private String entryTime;
    private String email;
    private String phoneNumber;
    private String password;
    private Long jobTitleCategoryId;
    private List<Long> permissionIds;
}
