package com.example.capstone_project.controller.body.project;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewProjectBody {
    @NotEmpty(message = "New project name can't be empty")
    @Pattern(regexp = "^[a-zA-Z ]{1,50}$", message = "Project name must only contain letters and be up to 50 characters long")
    private String projectName;
}
