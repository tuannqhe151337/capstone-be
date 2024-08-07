package com.example.capstone_project.controller.responses.expense.list;

import com.example.capstone_project.controller.responses.expense.CostTypeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {
    private Long expenseId;
    private String name;
    private CostTypeResponse costType;
    private BigDecimal unitPrice;
    private Integer amount;
    private String projectName;
    private String supplierName;
    private String pic;
    private String notes;
    private StatusResponse status;
}
