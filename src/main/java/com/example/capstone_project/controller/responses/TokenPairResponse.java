package com.example.capstone_project.controller.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPairResponse {
    private String accessToken;
    private String refreshToken;
}