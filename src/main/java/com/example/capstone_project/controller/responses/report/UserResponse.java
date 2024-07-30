package com.example.capstone_project.controller.responses.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long userId;
    private String username;
}
