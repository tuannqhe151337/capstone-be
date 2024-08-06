package com.example.capstone_project.repository.result;

import java.math.BigDecimal;

public interface YearDiagramResult {
    Integer getMonth();

    BigDecimal getActualCost();

    BigDecimal getExpectedCost();
}
