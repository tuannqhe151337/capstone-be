package com.example.capstone_project.controller.body.plan.reupload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class ReUploadExpenseBody {
    @NotEmpty(message = "Expense code can't be empty.")
    private String expenseCode;
    @NotEmpty(message = "Expense name can't be empty.")
    private String expenseName;
    @NotEmpty(message = "Expense cost type id can't be empty.")
    private long costTypeId;
    @NotEmpty(message = "Unit price can't be empty.")
    @Min(value = 0, message = "Unit price can't be negative")
    private BigDecimal unitPrice;
    @NotEmpty(message = "Amount can't be empty.")
    @Min(value = 0, message = "Amount can't be negative")
    private Integer amount;
    @NotEmpty(message = "Project name can't be empty.")
    private String projectName;
    @NotEmpty(message = "Suppler name can't be empty.")
    private String supplerName;
    @NotEmpty(message = "PIC can't be empty.")
    private String pic;
    private String notes;
}
