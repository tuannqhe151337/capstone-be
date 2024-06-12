package com.example.capstone_project.controller.responses.plan.detail;

import com.example.capstone_project.controller.responses.plan.DepartmentResponse;
import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.controller.responses.plan.TermResponse;
import com.example.capstone_project.controller.responses.plan.UserResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class PlanDetailResponse {
    private Long id;
    private String name;
    private BigDecimal biggestExpenditure;
    private BigDecimal totalPlan;
    private TermResponse term;
    private StatusResponse status;
    private LocalDateTime planDueDate;
    private LocalDateTime createdAt;
    private DepartmentResponse department;
    private UserResponse user;
    private String version;
}
