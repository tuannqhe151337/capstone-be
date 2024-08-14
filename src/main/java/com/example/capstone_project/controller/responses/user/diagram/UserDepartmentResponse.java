package com.example.capstone_project.controller.responses.user.diagram;

import com.example.capstone_project.controller.responses.user.DepartmentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDepartmentResponse {
    private DepartmentResponse department;
    private Integer numberUser;
}
