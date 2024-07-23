package com.example.capstone_project.controller.body.expense;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApprovalExpenseBody {
    @NotNull(message = "Term Id can not be null")
    private Long expenseId;
}
