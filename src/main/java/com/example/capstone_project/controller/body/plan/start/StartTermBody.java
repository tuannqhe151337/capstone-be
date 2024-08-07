package com.example.capstone_project.controller.body.plan.start;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StartTermBody {
    @NotNull(message = "Term Id can not be null")
    private Long termId;
}
