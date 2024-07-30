package com.example.capstone_project.controller.responses.report.list;

import com.example.capstone_project.controller.responses.report.DepartmentResponse;
import com.example.capstone_project.controller.responses.report.StatusResponse;
import com.example.capstone_project.controller.responses.report.TermResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportResponse {
    private long reportId;
    private String name;
    private StatusResponse status;
    private TermResponse term;
    private DepartmentResponse department;
}
