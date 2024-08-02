package com.example.capstone_project.controller.responses.report.detail;

import com.example.capstone_project.controller.responses.report.StatusResponse;
import com.example.capstone_project.controller.responses.report.TermResponse;
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
    private Long reportId;
    private String name;
    private TermResponse term;
    private StatusResponse status;
    private LocalDateTime createdAt;
}