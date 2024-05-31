package com.example.capstone_project.controller.responses.expense.list;

import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class ExpenseResponse {
    private long expenseId;
    private String name;
    private CostTypeResponse costType;
    private BigDecimal unitPrice;
    private int amount;
    private String projectName;
    private String supplierName;
    private String pic;
    private String notes;
    private StatusResponse status;
}
