package com.example.capstone_project.controller.responses.annualReport.diagram;

import com.example.capstone_project.controller.responses.annualReport.CostTypeResponse;
import com.example.capstone_project.controller.responses.annualReport.expenses.DepartmentResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CostTypeDiagramResponse {
    private CostTypeResponse costType;
    private BigDecimal totalCost;
}
