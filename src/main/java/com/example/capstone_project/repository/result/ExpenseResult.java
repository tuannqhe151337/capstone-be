package com.example.capstone_project.repository.result;

import com.example.capstone_project.utils.enums.ExpenseStatusCode;

import java.time.LocalDateTime;

public interface ExpenseResult {
    String getExpenseCode();

    String getExpenseName();

    LocalDateTime getDate();

    String getTerm();

    String getDepartmentName();

    String getExpense();

    String getCostTypeName();

    String getUnitPrice();

    String getAmount();

    String getTotal();

    String getProjectName();

    String getSupplierName();

    String getPic();

    String getNote();

    String getStatusName();

    Long getExpenseId();

    ExpenseStatusCode getStatusCode();

    Long getDepartmentId();

    Long getCostTypeId();

    Long getStatusId();
}