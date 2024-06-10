
package com.example.capstone_project.controller.responses.plan;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentResponse {
    private long departmentId;
    private String name;
}
