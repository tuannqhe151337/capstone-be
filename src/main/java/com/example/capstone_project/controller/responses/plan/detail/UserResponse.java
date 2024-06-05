package com.example.capstone_project.controller.responses.plan.detail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private long userId;
    private String username;
}
