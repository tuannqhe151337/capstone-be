package com.example.capstone_project.service.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalCostByCurrencyResult {
    private Long currencyId;
    private String date;
    private BigDecimal totalCost;
}
