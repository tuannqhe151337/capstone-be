package com.example.capstone_project.controller.body.expense;

import com.example.capstone_project.utils.enums.ExpenseStatusCode;
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
    @NotEmpty(message = "Expense id can't be empty")
    private Long expenseId;

    @NotNull(message = "Status code can't be null")
    private ExpenseStatusCode statusCode;
}
