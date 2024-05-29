package com.example.capstone_project.controller.responses.userManagement;

import com.example.capstone_project.controller.responses.auth.PositionResponse;
import com.example.capstone_project.controller.responses.auth.RoleResponse;
import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String username;
    private String email;
    private DepartmentResponse department;
    private RoleResponse role;
    private PositionResponse position;
    private boolean deactivate;
}