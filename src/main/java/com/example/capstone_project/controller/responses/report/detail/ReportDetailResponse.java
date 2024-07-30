package com.example.capstone_project.controller.responses.report.detail;

import com.example.capstone_project.controller.responses.report.DepartmentResponse;
import com.example.capstone_project.controller.responses.report.TermResponse;
import com.example.capstone_project.controller.responses.report.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailResponse {
    private Long id;
    private String name;
    private BigDecimal biggestExpenditure;
    private BigDecimal totalCost;
    private TermResponse term;
    private LocalDateTime planDueDate;
    private LocalDateTime createdAt;
    private DepartmentResponse department;
    private UserResponse user;
}