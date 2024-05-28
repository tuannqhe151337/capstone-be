package com.example.capstone_project.controller.body.confirm_expenses;

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
    @NotEmpty(message = "Term Id, can't not be empty")
    private String planName;
    @NotEmpty(message = "Term Id, can't not be empty")
    private String fileName;
    @NotNull(message = "Term Id, can't not be null")
    private List<ExpenseBody> expenses;
}
