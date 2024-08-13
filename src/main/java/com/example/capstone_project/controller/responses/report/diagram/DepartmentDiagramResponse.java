package com.example.capstone_project.controller.responses.report.diagram;

import com.example.capstone_project.controller.responses.report.DepartmentResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DepartmentDiagramResponse {
    private DepartmentResponse department;
    private BigDecimal totalCost;
}
