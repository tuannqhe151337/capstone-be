package com.example.capstone_project.controller.body.plan.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
@Data
@Builder
public class ExpenseBody {
    @NotEmpty(message = "Name can not be empty")
    @Size(max = 100, message = "Expense name must be less than 100 characters")
    private String name;

    @NotNull(message = "Cost type id can not be null")
    private Long costTypeId;

    @NotNull( message = "Unit price can't be null")
    @Range(min = 0, message = "Unit price can not be negative")
    private BigDecimal unitPrice;

    @NotNull(message = "Amount can not be null")
    @Min(value = 0, message = "Amount can not be negative")
    private Integer amount;

    @NotEmpty(message = "Project name can not be empty")
    @Size(max = 100, message = "Project name must be less than 100 characters")
    private String projectName;

    @NotEmpty(message = "Supplier name can not be empty")
    @Size(max = 100, message = "Supplier name must be less than 100 characters")
    private String supplierName;

    @NotEmpty(message = "PiC can not be empty")
    @Size(max = 100, message = "PIC must be less than 100 characters")
    private String pic;

    @Size(max = 200, message = "Note must be less than 200 characters")
    private String notes;
}
