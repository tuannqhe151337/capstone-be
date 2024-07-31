package com.example.capstone_project.controller.body.department;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDepartmentBody {
    @NotEmpty(message = "Department name can't be empty")
    @Pattern(regexp = "^[a-zA-Z ]{1,50}$", message = "Department name must only contain letters and be up to 50 characters long")
    private String departmentName;

    @NotNull(message = "Department id can't be empty")
    private Long departmentId;
}
