package com.example.capstone_project.controller.body.confirmExpenses;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConfirmNewExpenseBody {
    @NotEmpty(message = "Term Id, can't not be empty")
    private long termId;
    @NotEmpty(message = "Plan name, can't not be empty")
    private String planName;
    @NotEmpty(message = "File name, can't not be empty")
    private String fileName;
    @NotNull(message = "Expenses, can't not be null")
    private List<ExpenseBody> expenses;
}
