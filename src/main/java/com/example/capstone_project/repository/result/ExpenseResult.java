package com.example.capstone_project.repository.result;

import java.time.LocalDateTime;

public interface ExpenseResult {
    String getExpenseCode();
    LocalDateTime getDate();
    String getTerm();
    String getDepartment();
    String getExpense();
    String getCostType();
    String getUnitPrice();
    String getAmount();
    String getTotal();
    String getProjectName();
    String getSupplierName();
    String getPic();
    String getNote();
    String getStatus();
}
