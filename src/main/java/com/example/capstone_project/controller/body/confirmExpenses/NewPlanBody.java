package com.example.capstone_project.controller.body.confirmExpenses;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NewPlanBody {
    @NotEmpty(message = "Term Id, can not be empty")
    private long termId;
    @NotEmpty(message = "Plan name, can not be empty")
    private String planName;
    @NotEmpty(message = "File name, can not be empty")
    private String fileName;
    @NotNull(message = "Expenses, can not be null")
    private List<ExpenseBody> expenses;
}
