package com.example.capstone_project.controller.body.plan.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class NewPlanBody {
    @NotNull(message = "Term Id can not be null")
    private Long termId;

    @NotEmpty(message = "Plan name can not be empty")
    @Size(max = 100, message = "Plan name must be less than 100 characters")
    private String planName;

    @NotEmpty(message = "File name can not be empty")
    @Size(max = 100, message = "File name must be less than 100 characters")
    private String fileName;

    @NotNull(message = "List expense can not be null")
    @NotEmpty(message = "List expense can not be empty")
    private List<ExpenseBody> expenses;
}
