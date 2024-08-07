package com.example.capstone_project.controller.responses.annualReport.expenses;

import com.example.capstone_project.controller.responses.annualReport.CostTypeResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class AnnualReportExpenseResponse {
    private Long expenseId;
    private DepartmentResponse department;
    private BigDecimal totalExpenses;
    private BigDecimal biggestExpenditure;
    private CostTypeResponse costType;

}
