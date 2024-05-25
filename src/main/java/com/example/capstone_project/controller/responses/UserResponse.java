package com.example.capstone_project.controller.responses;

import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Position;
import com.example.capstone_project.entity.Role;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private LocalDate dob;
    private Department department;   //
    private Role role;
    private String note;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Position position;
    private Boolean status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
