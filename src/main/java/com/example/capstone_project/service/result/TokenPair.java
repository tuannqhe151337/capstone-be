package com.example.capstone_project.service.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPair {
    private String accessToken;
    private String refreshToken;
}
