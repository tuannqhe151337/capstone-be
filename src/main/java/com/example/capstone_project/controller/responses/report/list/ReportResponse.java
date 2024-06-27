package com.example.capstone_project.controller.responses.report.list;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportResponse {
    private Long reportId;
    private String name;
    private String version;
    private StatusResponse status;
    private TermResponse term;
    private DepartmentResponse department;
}
