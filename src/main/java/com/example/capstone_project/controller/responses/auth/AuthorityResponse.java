package com.example.capstone_project.controller.responses.auth;

import lombok.Data;

@Data
public class AuthorityResponse {
    private Long id;

    private String code;

    private String name;
}