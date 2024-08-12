package com.example.capstone_project.service.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


public interface TotalCostByCurrencyResult {
    Long getCurrencyId();

    Integer getMonth();

    Integer getYear();

    BigDecimal getTotalCost();
}
