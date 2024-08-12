package com.example.capstone_project.service;

import com.example.capstone_project.entity.CurrencyExchangeRate;
import com.example.capstone_project.service.result.ExchangeResult;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.TreeMap;

public interface CurrencyExchangeRateService {
    TreeMap<String, List<ExchangeResult>> getListMonthlyExchangePaging(String query, Integer year, Pageable pageable);

    long countDistinctListMonthlyExchangePaging(Integer year, Pageable pageable);

    void createMonthlyExchange(String month, List<CurrencyExchangeRate> exchangeRates);

    void deleteMonthlyExchange(String monthYear);

    void updateExchange(CurrencyExchangeRate currencyExchangeRate);

    void createExchange(String monthYear, Long currencyId, BigDecimal amount);
}
