package com.example.capstone_project.controller.responses.plan.detail;

import com.example.capstone_project.controller.responses.plan.DepartmentResponse;
import com.example.capstone_project.controller.responses.plan.TermResponse;
import com.example.capstone_project.controller.responses.plan.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanDetailResponse {
    private Long id;
    private String name;
    private CostResponse actualCost;
    private CostResponse expectedCost;
    private TermResponse term;
    private LocalDateTime createdAt;
    private DepartmentResponse department;
    private UserResponse user;
    private Integer version;
}
