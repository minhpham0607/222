package com.example.hrms.biz.department.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    private Long departmentId;
    private String departmentName;
    private String roleName;
    private String userName; // Add this field
}