package com.example.capstone_project.service.result;

import com.example.capstone_project.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResult {
    private TokenPair tokenPair;
    private User user;
}
