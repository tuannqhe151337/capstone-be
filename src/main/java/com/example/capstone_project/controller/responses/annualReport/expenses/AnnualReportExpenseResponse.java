package com.example.capstone_project.controller.responses.annualReport.expenses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class AnnualReportExpenseResponse {
    private Long expenseId;
    private String departmentName;
    private BigDecimal totalExpenses;
    private BigDecimal biggestExpenditure;
    private CostTypeResponse costType;

}
