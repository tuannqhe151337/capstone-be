package com.example.capstone_project.controller.body.plan.detail;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanDetailBody {
    @NotNull(message = "Plan Id need to delete can't be empty")
    private Long planId;
}
