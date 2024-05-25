package com.example.capstone_project.controller.responses.auth;

import com.example.capstone_project.controller.responses.AuthorityResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDataResponse {
    private long userId;
    private RoleResponse role;
    private DepartmentResponse department;
    private List<AuthorityResponse> authorities;
    private UserSettingResponse settings;
}
