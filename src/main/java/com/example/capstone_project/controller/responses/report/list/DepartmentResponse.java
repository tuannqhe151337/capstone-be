package com.example.capstone_project.controller.responses.report.list;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentResponse {
    private Long departmentId;
    private String name;
}
