package com.example.capstone_project.controller.body.confirmExpenses;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
@Data
@Builder
public class ExpenseBody {
    @NotEmpty(message = "Name, can not be empty")
    private String name;
    @NotEmpty(message = "Cost type name, can not be empty")
    private String costTypeName;
    @NotEmpty()
    @Range(min = 0, message = "Unit price can not be negative")
    private BigDecimal unitPrice;
    @NotEmpty(message = "Amount, can not be empty")
    @Min(value = 0, message = "Amount can not be negative")
    private int amount;
    @NotEmpty(message = "Project name, can not be empty")
    private String projectName;
    @NotEmpty(message = "Supplier name, can not be empty")
    private String supplierName;
    @NotEmpty(message = "PiC, can not be empty")
    private String pic;
    private String notes;
}
