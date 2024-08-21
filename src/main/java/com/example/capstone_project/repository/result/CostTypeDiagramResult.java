package com.example.capstone_project.repository.result;

import java.math.BigDecimal;

public interface CostTypeDiagramResult {
    String getMonth();
    Long getCostTypeId();
    String getCostTypeName();
    BigDecimal getTotalCost();
}
