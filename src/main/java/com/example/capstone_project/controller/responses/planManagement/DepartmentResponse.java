package com.example.capstone_project.controller.responses.planManagement;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentResponse {
    private long departmentId;
    private String name;
}
