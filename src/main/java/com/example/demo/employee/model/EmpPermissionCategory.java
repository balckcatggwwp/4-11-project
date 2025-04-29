package com.example.demo.employee.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "empPermissionCategory")
public class EmpPermissionCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empPermissionTypeId;

    @Column(name = "emp_permission_type_name", nullable = false, length = 50)
    private String empPermissionTypeName; // 例如: "會員管理", "訂票管理", "餐飲管理"
}

