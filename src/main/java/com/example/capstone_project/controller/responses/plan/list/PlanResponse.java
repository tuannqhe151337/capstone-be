package com.example.capstone_project.controller.responses.plan.list;

import com.example.capstone_project.controller.responses.plan.DepartmentResponse;
import com.example.capstone_project.controller.responses.plan.StatusResponse;
import com.example.capstone_project.controller.responses.plan.TermResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PlanResponse {
    private Long planId;
    private String name;
    private TermResponse term;
    private StatusResponse status;
    private DepartmentResponse department;
    private String version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
