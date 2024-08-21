package com.example.capstone_project.controller.responses.report.diagram;

import com.example.capstone_project.controller.responses.report.CostTypeResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CostTypeDiagramResponse {
    private CostTypeResponse costType;
    private BigDecimal totalCost;
}
