package com.example.capstone_project.service;

import com.example.capstone_project.entity.Currency;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CurrencyService {
    List<Currency> getListCurrencyPaging(String query, Pageable pageable);

    long countDistinctListCurrencyPaging(String query);

    void createCurrency(Currency currency);

    void deleteCurrency(Long currencyId);

    void updateCurrency(Currency currency);

    List<Currency> getListCurrency();

}
