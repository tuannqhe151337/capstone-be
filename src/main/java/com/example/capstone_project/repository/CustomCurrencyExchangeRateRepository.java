package com.example.capstone_project.repository;

import com.example.capstone_project.entity.CurrencyExchangeRate;
import com.example.capstone_project.repository.result.PaginateExchange;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCurrencyExchangeRateRepository {
    List<PaginateExchange> getMonthYearPaginated(Integer year, Pageable pageable);

    List<CurrencyExchangeRate> getListCurrencyExchangeRateByMonthYear(List<PaginateExchange> paginateExchanges);

    List<CurrencyExchangeRate> getListCurrencyExchangeRateByMonthYear(List<PaginateExchange> paginateExchanges, List<Long> currencyIds);

    long countDistinctListExchangePaging(Integer year);
}
