package com.example.capstone_project.controller.responses.annualReport.list;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AnnualReportResponse {
    private Long annualReportId;
    private String name;
    private Integer totalTerm;
    private BigDecimal totalExpense;
    private Integer totalDepartment;
    private LocalDateTime createDate;
}
