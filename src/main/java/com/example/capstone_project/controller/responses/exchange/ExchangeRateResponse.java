package com.example.capstone_project.controller.responses.exchange;

import com.example.capstone_project.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateResponse {
    private CurrencyResponse currency;
    private BigDecimal amount;
}
