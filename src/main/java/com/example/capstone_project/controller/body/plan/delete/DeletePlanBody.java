package com.example.capstone_project.controller.body.plan.delete;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeletePlanBody {
    @NotEmpty(message = "Plan Id need to delete can't be empty")
    private long planId;
}
