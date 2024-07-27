package com.example.capstone_project.repository.result;

import java.math.BigDecimal;

public interface CostTypeDiagramResult {
    Long getCostTypeId();
    String getCostTypeName();
    BigDecimal getTotalCost();
}
