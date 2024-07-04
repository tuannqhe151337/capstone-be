package com.example.capstone_project.repository.result;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ReportDetailResult {
    Long getReportId();
    String getName();
    BigDecimal getBiggestExpenditure();
    BigDecimal getTotalCost();
    Long getTermId();
    String getTermName();
    Long getStatusId();
    String getStatusName();
    String getStatusCode();
    LocalDateTime getPlanDueDate();
    LocalDateTime getCreatedAt();
    Long getDepartmentId();
    String getDepartmentName();
    Long getUserId();
    String getUsername();
}
