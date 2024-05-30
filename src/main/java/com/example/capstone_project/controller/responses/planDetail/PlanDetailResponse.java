package com.example.capstone_project.controller.responses.planDetail;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Builder
public class PlanDetailResponse {
    private long planId;
    private String termName;
    private String version;
    private StatusPlanDetailResponse status;
    private BigDecimal biggestExpenditure;
    private BigDecimal totalPlan;
    private LocalDate planDueDate;
    private LocalDate createAt;
    private String departmentName;
    private String createBy;
}
