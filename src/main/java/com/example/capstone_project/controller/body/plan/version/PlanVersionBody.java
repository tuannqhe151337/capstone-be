package com.example.capstone_project.controller.body.plan.version;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanVersionBody {
    @NotNull(message = "Plan Id can't be empty")
    private Long planId;
}
