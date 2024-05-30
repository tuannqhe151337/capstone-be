package com.example.capstone_project.controller.responses.userManagement;

import com.example.capstone_project.controller.responses.DepartmentResponse;
import com.example.capstone_project.controller.responses.PositionResponse;
import com.example.capstone_project.controller.responses.RoleResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime dob;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String note;
    private String fullName;
    private String phoneNumber;
    private String address;

    //department
    @JsonProperty("department")
    private DepartmentResponse departmentResponse;

    //position
    @JsonProperty("position")
    private PositionResponse positionResponse;

    //role
    @JsonProperty("role")
    private RoleResponse roleResponse;


}


