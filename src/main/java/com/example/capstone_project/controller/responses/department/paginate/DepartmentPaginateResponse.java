package com.example.capstone_project.controller.responses.department.paginate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentPaginateResponse {
    private long departmentId;
    private String name;
}
