package com.example.capstone_project.repository.result;

import com.example.capstone_project.entity.Currency;
import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportExpenseResult {
    long expenseId;
    String expenseCode;
    String expenseName;
    long costTypeId;
    String costTypeName;
    BigDecimal unitPrice;
    int amount;
    long projectId;
    String projectName;
    long supplierId;
    String supplierName;
    long picId;
    String picName;
    String note;
    long statusId;
    String statusCode;
    String statusName;
    long departmentId;
    String departmentName;
    Currency currency;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
