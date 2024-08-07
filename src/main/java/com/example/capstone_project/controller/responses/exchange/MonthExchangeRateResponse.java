package com.example.capstone_project.controller.responses.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthExchangeRateResponse {
    private String month;
    private List<ExchangeRateResponse> exchangeRates;
}
