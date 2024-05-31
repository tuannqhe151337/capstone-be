package com.example.capstone_project.controller.responses.user.create;

import com.example.capstone_project.controller.responses.user.DepartmentResponse;
import com.example.capstone_project.controller.responses.user.PositionResponse;
import com.example.capstone_project.controller.responses.user.RoleResponse;
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
    private DepartmentResponse department;

    //position
    @JsonProperty("position")
    private PositionResponse position;

    //role
    @JsonProperty("role")
    private RoleResponse role;


}


