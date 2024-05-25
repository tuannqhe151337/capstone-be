package com.example.capstone_project.controller.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;
import org.json.JSONPropertyName;

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
    @JsonProperty("department")
    private DepartmentResponse departmentResponse;

    //position
    @JsonProperty("position")
    private PositionResponse positionResponse;

    //role
    @JsonProperty("role")
    private RoleResponse roleResponse;


}


