package com.example.demo.employee.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "empPermission")
public class EmpPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long EmpPermissionId;

    @ManyToOne
    @JoinColumn(name = "empId") // 員工ID
    private Employee employee;
    
    @ManyToOne
    @JoinColumn(name = "empPermissionTypeId") // 權限分類ID
    private EmpPermissionCategory empPermissionCategory;
}
