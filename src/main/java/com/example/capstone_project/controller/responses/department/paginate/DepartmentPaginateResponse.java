package com.example.capstone_project.controller.responses.department.paginate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentPaginateResponse {
    private long departmentId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
