package com.example.capstone_project.repository.result;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PlanDetailResult {
     Long getPlanId();
     String getName();
     BigDecimal getBiggestExpenditure();
     BigDecimal getTotalPlan();
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
