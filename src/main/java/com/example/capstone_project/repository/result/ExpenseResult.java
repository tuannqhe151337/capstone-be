package com.example.capstone_project.repository.result;

import com.example.capstone_project.utils.enums.ExpenseStatusCode;

public interface ExpenseResult {
    Long getExpenseId();
    String getExpenseCode();
    ExpenseStatusCode getStatusCode();
}
