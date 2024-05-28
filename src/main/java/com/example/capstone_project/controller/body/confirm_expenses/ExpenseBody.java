package com.example.capstone_project.controller.body.confirm_expenses;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class ExpenseBody {
    private String name;
    private String costTypeName;
    private BigDecimal unitPrice;
    private int amount;
    private String projectName;
    private String supplierName;
    private String pic;
    private String notes;
}
