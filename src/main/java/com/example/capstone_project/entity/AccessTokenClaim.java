package com.example.capstone_project.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class AccessTokenClaim {
    private long userId;
    private long departmentId;
    private String roleCode;
}
