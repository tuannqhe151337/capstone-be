package com.example.capstone_project.controller.responses.report.calculate;

import com.example.capstone_project.controller.responses.report.CurrencyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportActualCostResponse {
    BigDecimal cost;
    CurrencyResponse currency;
}
