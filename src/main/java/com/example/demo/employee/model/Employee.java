package com.example.demo.employee.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "EMPLOYEE")
public class Employee {
//	個人訊息
	@Id
	@UuidGenerator
	private UUID empId;
	
	@Column(name = "name",length = 15,nullable = false)
	private String name;
	
	@Column(name = "gender",nullable = false)
	private String gender;
	
	@Column(name = "nationalId",nullable = false)
	private String nationalId;
	
//	認證相關
	@Column(name = "email",nullable = false)
	private String email;
	
	@Column(name = "phoneNumber",nullable = false,length = 10,unique = true)
	private String phoneNumber;
	
	@Column(name = "password",nullable = false, length = 255)
	private String password;
//	日期相關
	
	@Column(name = "entryTime", nullable = false)
    private String entryTime;
	
	@Column(name = "dateOfBirth", nullable = false)
    private String dateOfBirth;
	
	@ManyToOne
	@JoinColumn(name = "jobTitleId")
	private JobTitleCategory jobTitleCategory;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "empPermission",
        joinColumns = @JoinColumn(name = "empId"),
        inverseJoinColumns = @JoinColumn(name = "empPermissionTypeId")
    )
    private Set<EmpPermissionCategory> permissions = new HashSet<>();
}
