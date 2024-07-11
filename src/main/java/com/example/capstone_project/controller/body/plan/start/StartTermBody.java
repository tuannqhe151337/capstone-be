package com.example.capstone_project.controller.body.plan.start;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class StartTermBody {
    @NotNull(message = "Term Id can not be null")
    private Long termId;
}
