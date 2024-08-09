package com.example.capstone_project.controller.responses.report.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearDiagramResponse {
    private Integer month;
    private BigDecimal expectedCost;
    private BigDecimal actualCost;
}
