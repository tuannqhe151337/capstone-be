package com.example.capstone_project.controller.body.plan.reupload;

import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class ReUploadExpenseBody {
    @NotEmpty(message = "Expense code can't be empty.")
    @Size(max = 100, message = "Expense code must be less than 100 characters")
    private String expenseCode;

    @NotEmpty(message = "Expense name can't be empty.")
    @Size(max = 100, message = "Expense name must be less than 100 characters")
    private String expenseName;

    @NotNull(message = "Expense cost type id can't be null.")
    private Long costTypeId;

    @NotNull(message = "Unit price can't be null.")
    @Min(value = 0, message = "Unit price can't be negative")
    private BigDecimal unitPrice;

    @NotNull(message = "Amount can't be null.")
    @Min(value = 0, message = "Amount can't be negative")
    private Integer amount;

    @NotEmpty(message = "Project name can't be empty.")
    @Size(max = 100, message = "Project name must be less than 100 characters")
    private String projectName;

    @NotEmpty(message = "Supplier name can't be empty.")
    @Size(max = 100, message = "Supplier name must be less than 100 characters")
    private String supplierName;

    @NotEmpty(message = "PIC can't be empty.")
    @Size(max = 100, message = "PIC must be less than 100 characters")
    private String pic;

    @Size(max = 200, message = "Notes must be less than 100 characters")
    private String notes;
}
