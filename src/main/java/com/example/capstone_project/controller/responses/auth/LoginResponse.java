package com.example.capstone_project.controller.responses.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String refreshToken;
    private UserDataResponse data;
}