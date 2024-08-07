package com.example.capstone_project.controller.responses.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class UserDataResponse {
    private long userId;

    private String username;

    private String fullName;

    private String phoneNumber;

    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dob;

    private String address;

    private RoleResponse role;

    private DepartmentResponse department;

    private PositionResponse position;

    private List<AuthorityResponse> authorities;

    private UserSettingResponse settings;
}
