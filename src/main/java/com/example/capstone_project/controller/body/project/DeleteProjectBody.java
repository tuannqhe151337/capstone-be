package com.example.capstone_project.controller.body.project;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteProjectBody {
    @NotNull(message = "Project id can't be empty")
    private Long projectId;
}
