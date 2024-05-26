package com.example.capstone_project.controller.responses.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPairResponse {
    private String accessToken;
    private String refreshToken;
}