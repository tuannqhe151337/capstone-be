package com.example.capstone_project.repository;

import com.example.capstone_project.entity.CurrencyExchangeRate;
import com.example.capstone_project.repository.result.PaginateExchange;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCurrencyExchangeRateRepository {
    List<PaginateExchange> getExchangeWithPagination(Integer year, Pageable pageable);

    List<CurrencyExchangeRate> getListCurrencyExchangeRate(List<PaginateExchange> paginateExchanges);
}
