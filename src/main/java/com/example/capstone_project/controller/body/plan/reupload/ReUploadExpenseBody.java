package com.example.capstone_project.controller.body.plan.reupload;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class ReUploadExpenseBody {
    private String expenseCode;
    private String expenseName;
    private long costTypeId;
    private BigDecimal unitPrice;
    private Integer amount;
    private String projectName;
    private String supplerName;
    private String pic;
    private String notes;
}
