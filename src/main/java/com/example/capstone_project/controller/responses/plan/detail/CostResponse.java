package com.example.capstone_project.controller.responses.plan.detail;

import com.example.capstone_project.controller.responses.plan.CurrencyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CostResponse {
    private BigDecimal cost;
    private CurrencyResponse currency;

}
