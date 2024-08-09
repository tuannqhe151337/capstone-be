package com.example.capstone_project.service;

import com.example.capstone_project.entity.CurrencyExchangeRate;
import com.example.capstone_project.service.result.ExchangeResult;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public interface CurrencyExchangeRateService {
    TreeMap<String, List<ExchangeResult>> getListExchangePaging(String query, Integer year, Pageable pageable);

    long countDistinctListExchangePaging(Integer year, Pageable pageable);

    void createExchange(List<CurrencyExchangeRate> exchangeRates);

    void deleteExchange(Long exchangeId);

    void updateExchange(CurrencyExchangeRate build);

    List<CurrencyExchangeRate> getListExchange();

}
