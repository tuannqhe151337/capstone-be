package com.example.capstone_project.controller.responses.user.create;

import com.example.capstone_project.controller.responses.user.DepartmentResponse;
import com.example.capstone_project.controller.responses.user.PositionResponse;
import com.example.capstone_project.controller.responses.user.RoleResponse;
import com.example.capstone_project.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email is not valid")
    private String email;

    @NotNull(message = "DOB cannot be null")
    private LocalDateTime dob;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String note;

    @Size(max = 100, message = "Full name must be less than 100 characters")
    @Column(name = "full_name")
    private String phoneNumber;

    @NotEmpty(message = "Address cannot be empty")
    @Size(max = 200, message = "Address must be less than 200 characters")
    private String address;

    //department
    @NotNull(message = "Department cannot be null")
    @JsonProperty("department")
    private DepartmentResponse department;

    //position
    @NotNull(message = "Position cannot be null")
    @JsonProperty("position")
    private PositionResponse position;

    //role
    @NotNull(message = "Role cannot be null")
    @JsonProperty("role")
    private RoleResponse role;



}





