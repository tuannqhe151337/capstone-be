package com.example.capstone_project.controller.responses.report.approval;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCodeResponse {
    private Long expenseId;
    private String expenseCode;
}
