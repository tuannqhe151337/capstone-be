package com.example.capstone_project.repository.result;

import java.math.BigDecimal;

public interface AnnualReportResult {
    String getReportName();

    Integer getTotalTerm();

    BigDecimal getTotalExpense();

    Integer getDepartment();
}
