package com.example.capstone_project.controller.responses.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanResponse {
    private Long id;
    private String name;
    private TermResponse term;
    private StatusResponse status;
    private DepartmentResponse department;
    private String version;
}
