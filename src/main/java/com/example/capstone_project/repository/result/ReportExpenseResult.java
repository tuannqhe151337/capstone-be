package com.example.capstone_project.repository.result;

import com.example.capstone_project.utils.enums.ExpenseStatusCode;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportExpenseResult {
    long expenseId;
    String expenseName;
    long costTypeId;
    String costTypeName;
    BigDecimal unitPrice;
    int amount;
    String projectName;
    String supplierName;
    String pic;
    String note;
    long statusId;
    String statusCode;
    String statusName;
    long departmentId;
    String departmentName;
}
