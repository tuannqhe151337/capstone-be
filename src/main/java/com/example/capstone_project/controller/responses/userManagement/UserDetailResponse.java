package com.example.capstone_project.controller.responses.userManagement;

import com.example.capstone_project.controller.responses.DepartmentResponse;
import com.example.capstone_project.controller.responses.PositionResponse;
import com.example.capstone_project.controller.responses.RoleResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Email(message = "Invalid email address")
    private String email;

    @Past(message = "Date of birth must be in the past")
    private LocalDateTime dob;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String note;

    @NotBlank(message = "Full name cannot be empty")
    private String fullName;

    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    // Department
    @NotNull(message = "Department cannot be empty")
    private DepartmentResponse departmentResponse;

    // Position
    @NotNull(message = "Position cannot be empty")
    private PositionResponse positionResponse;

    // Role
    @NotNull(message = "Role cannot be empty")
    private RoleResponse roleResponse;

}


