package com.example.capstone_project.controller.body.expense;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseBody {
    @NotEmpty(message = "Expense code can't be empty")
    private String expenseCode;
    @NotNull(message = "Status id can't be null")
    private Long statusId;
}
