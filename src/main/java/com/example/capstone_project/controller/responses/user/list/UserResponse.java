package com.example.capstone_project.controller.responses.user.list;

import com.example.capstone_project.controller.responses.auth.PositionResponse;
import com.example.capstone_project.controller.responses.auth.RoleResponse;
import com.example.capstone_project.controller.responses.user.DepartmentResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long userId;

    private String username;

    private String email;

    private DepartmentResponse department;

    private RoleResponse role;

    private PositionResponse position;

    private boolean deactivate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime updatedAt;
}