package com.example.capstone_project.controller.responses.department.paginate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentPaginateResponse {
    private Long departmentId;
    private String name;
}
