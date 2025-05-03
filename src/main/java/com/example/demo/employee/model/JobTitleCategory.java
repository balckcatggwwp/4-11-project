package com.example.demo.employee.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jobTitleCategory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobTitleCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long JobTitleCategoryId;

    @Column(name = "jobTitleName", nullable = false, length = 50)
    
    private String jobTitleName; // 例："客服"、"影城主管"、"經理"
    
    @Column(name = "jobLevel", nullable = false)
    private int jobLevel; // 1~5，數字越高權限越高
}
