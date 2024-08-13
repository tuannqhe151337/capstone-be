package com.example.capstone_project.controller.responses.report.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearCostTypeDiagramResponse {
    String month;
    List<CostTypeDiagramResponse> diagramResponses = new ArrayList<>();
}
