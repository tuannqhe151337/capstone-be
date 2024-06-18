package com.example.capstone_project.entity;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDetail {
    private long departmentId;
    private String roleCode;
}
