package com.example.capstone_project.controller.body.plan.submit;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmitPlanBody {
    @NotNull(message = "Plan id need to submit can't be null")
    private Long planId;
}