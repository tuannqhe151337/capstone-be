package com.example.capstone_project.repository.result;

import java.math.BigDecimal;

public interface ExchangeRateResult {
    String getDate();

    BigDecimal getAmount();

    Long getCurrencyId();

}
