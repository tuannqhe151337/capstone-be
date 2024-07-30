package com.example.capstone_project.controller.body.plan.expenses;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanExpensesBody {
    @NotNull(message = "Plan Id need to see list expenses can't be empty")
    private Long planId;
}
