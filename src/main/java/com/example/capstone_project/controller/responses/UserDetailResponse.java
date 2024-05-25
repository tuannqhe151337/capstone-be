package com.example.capstone_project.controller.responses;

import com.example.capstone_project.entity.Role;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    private Long id;
    private String username;
    private String email;
    private LocalDate dob;

    private Boolean status;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private String note;
    private String fullName;
    private String phoneNumber;
    private String address;
    //department
    private Long departmentId;
    private String departmentName;
    //position
    private Long positionId;
    private String positionName;
    //role
    private Long roleId;
    private String roleCode;
    private String roleName;

}
