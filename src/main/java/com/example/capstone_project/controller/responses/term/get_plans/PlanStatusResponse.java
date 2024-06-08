package com.example.capstone_project.controller.responses.term.get_plans;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlanStatusResponse {
    @NotNull(message = "Id cannot be null")
    private Long id;
    @NotEmpty(message = "Name cannot be empty")
    private String name;
}
