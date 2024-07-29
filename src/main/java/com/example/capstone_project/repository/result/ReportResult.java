package com.example.capstone_project.repository.result;

import java.math.BigDecimal;

public interface ReportResult {
    Long getDepartmentId();
    BigDecimal getTotalExpense();
    BigDecimal getBiggestExpense();
    Long getCostTypeId();
}
