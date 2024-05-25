package com.example.capstone_project.controller.responses.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse {
    private long id;
    private String code;
    private String name;
}
