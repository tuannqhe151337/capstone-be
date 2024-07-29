package com.example.capstone_project.controller.responses.annualReport.list;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
public class AnnualReportResponse {
    private Long annualReportId;
    private String year;
    private Integer totalTerm;
    private BigDecimal totalExpense;
    private Integer totalDepartment;
    private LocalDateTime createDate;
}
